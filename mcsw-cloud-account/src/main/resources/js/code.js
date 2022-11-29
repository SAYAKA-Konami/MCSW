function generatePasswd (passwd){
    setMaxDigits(131);
    var key = new RSAKeyPair("010001", '', "008e9fdac2a933c27a8262eb0ab8004aa74571e1e7c27beb436ce17c37df778d8861a9a2afddc04a6e80da995e34754e1e002864f2480f0471257880b55359e8232601244593333eb9f0f99b894fe13538a80bfd14aeb94bb8108959140231195a9e9f488f7d5cc72a112d6a19576cb05eaf629435538907ccc9b008d64595646d");
    var result = encryptedString(key, encodeURIComponent(passwd));
    return result;
}

