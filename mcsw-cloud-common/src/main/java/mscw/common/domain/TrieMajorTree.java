package mscw.common.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @apiNote 字典树。记录专业。并且提供压缩功能。
 * @author wu nan
 * @since  2022/11/28
 **/
@Component("majorTree")
public class TrieMajorTree {
    /**
     *  根节点
     */
    private Node root;
    @Getter
    class Node{
        // 当前节点的字
        char word;
        /**
         * 在链表中的位置。这个特性需要由该树不进行实际删除操作来保证其效率。
         */
        @Setter
        int position;
        // 下一层节点
        List<Node> next;
        /**
         * 快速定位到对应位置的节点
         */
        Map<Integer, Node> next_num2node;
        /**
         *  根据字索引
         */
        Map<Character, Node> next_char2node;


        public Node(char v, int p) {
            this.next = new LinkedList<>();
            this.next_num2node = new HashMap<>();
            this.next_char2node = new HashMap<>();
            word = v;
            this.position = p;
        }
    }

    public TrieMajorTree() {
        this.root = new Node('$', 0);
    }

    /**
     *  在字典树中添加专业名称
     */
    public void add(String major){
        add(major, 0, root);
    }

    /**
     *  压缩。索引之间用 _ 隔开
     * @return null: If is not exist.
     */
    public String encode(String major){
        String encode = encode(major, root, new StringBuilder(), 0);
        return encode;
    }

    /**
     *  返回对应的专业名称
     * @param code 压缩后的字段
     */
    public String decode(String code){
        String[] split = code.split("_");
        return decode(split, root, new StringBuilder(), 0);
    }

    private String decode(String[] s, Node root, StringBuilder sb, int index){
        if (index >= s.length) return sb.toString();
        int idx = Integer.parseInt(s[index]);
        Node node = root.getNext_num2node().get(idx);
        sb.append(node.getWord());
        return decode(s, node, sb, index);
    }

    private String encode(String s, Node root, StringBuilder sb, int index){
        if (index >= s.length()) return sb.toString();
        char c = s.charAt(index);
        Node node = root.getNext_char2node().get(c);
        int position = node.getPosition();
        sb.append(position).append("_");
        return encode(s, node, sb, ++index);
    }

    /**
     * 递归添加
     */
    private void add(String c, int index, Node root){
        if (index >= c.length()) return;
        char cur = c.charAt(index);
        Map<Character, Node> next_char2node = root.getNext_char2node();
        Node node = null;
        if (next_char2node.containsKey(cur)){
            node = next_char2node.get(cur);
        }else{
            List<Node> next = root.getNext();
            int sizeAfterInsert = next.size() + 1;
            node = new Node(cur, sizeAfterInsert);
            next.add(node);
            root.getNext_num2node().put(sizeAfterInsert, node);
            root.getNext_char2node().put(cur, node);
        }
        add(c, ++index, node);
    }


}
