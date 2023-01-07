package mscw.common.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import redis.clients.jedis.Connection;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.commands.ProtocolCommand;
import redis.clients.jedis.util.SafeEncoder;

import java.util.List;

import static mscw.common.util.JedisUtil.CellCommand.CLTHROTTLE;
/**
 * @apiNote 由于Redis的衍生模块在常见的工具类中并无封装。所以提供这个工具类用于执行扩展命令。
 * @author wu nan
 * @since  2023/1/7
 **/
public class JedisUtil {
    private final JedisPool jedisPool;

    public JedisUtil(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }
    protected enum CellCommand implements ProtocolCommand {
        /**
         *  Redis-cell 中的唯一命令
         */
        CLTHROTTLE("CL.THROTTLE");

        private final byte[] raw;

        CellCommand(String alt) {
            raw = SafeEncoder.encode(alt);
        }

        @Override
        public byte[] getRaw() {
            return raw;
        }
    }

    @Data
    @AllArgsConstructor
    public class CellAnswer{
        /*
         * 0 代表允许， 1代表失败
         */
        private long result;
        private long capacity;
        /**
         * 漏斗剩余容量
         */
        private long leftQuota;
        /**
         * 如果当前操作被拒绝，则该参数表示至少需要等待多少秒
         */
        private long wait;
        /**
         *  多少秒后漏斗为空
         */
        private long timeToGetEmpty;
    }

    /**
     * 语法糖，再包裹一层
     */
    public CellAnswer cell(String ...value){
       return this.cell(value[0], value[1], value[2], value[3]);
    }

    /**
     *  操作redis-cell的简单封装。该模块使用“漏斗”算法。
     * @param key       指定“漏斗”
     * @param capacity  漏斗初始容量
     * @param number    单位时间允许释放的容量大小
     * @param time      规定单位时间
     */
    public CellAnswer cell(String key, String capacity, String number, String time){
        Jedis jedis = jedisPool.getResource();
        Connection connection = jedis.getConnection();
        connection.sendCommand(CLTHROTTLE, key, capacity, number, time);
        List<Long> replies = connection.getIntegerMultiBulkReply();
        return new CellAnswer(replies.get(0), replies.get(1), replies.get(2), replies.get(3),
                replies.get(4));
    }
}

