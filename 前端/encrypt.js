//初始化 rsa加密解密类
var crypt = new JSEncrypt();
//一般使用公钥加密,私钥解密
crypt.setPublicKey("-----BEGIN PUBLIC KEY-----\n"+
"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDZVnArkAz4rxQqaccqT4lLmIp3\n"+
"MBnQmrdQXEtsxFYZavu54KRpryWWhoXLW2uD5xamkeByGeNIRsZ09c0Lf5gFEQV5\n"+
"v+dIagwuKwHdMofgiJRuzqTBZon2BT1U1NU4wGwU6+2EA8LC07WXDG7MLpMkZAx5\n"+
"c5FoVYTpQeyh/9yzCQIDAQAB\n"+
"-----END PUBLIC KEY-----");

//注意,正式使用中,不应该有私钥放在js中 
crypt.setPrivateKey("-----BEGIN PRIVATE KEY-----\n" +
        "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBANRyqaBTovuEA3Hm\n" +
        "9zi33aH/nslZj2zXrVe42qCPQmSB2deHZ+tmzKZpgd8QdyCWZdNKgjLBF8mqNdFG\n" +
        "PX+EzP+XWfbYjtt8cR3KA89Ba2pCKWtXznZXkZc8KdzpvbkRCdFViD0itLTRApqN\n" +
        "a93PqPIewfEZtGUQ1Rqyxgzn72ZbAgMBAAECgYA1WQnBad8ue4sF6jLAemNcT71G\n" +
        "4IeElHBB6/hygybv6C+U0LrGwQy46RuksRTJCRtOwJILPrPDf0t+Xr4IrIdxXfgD\n" +
        "/WjPtDfX3Re7qtMItJPkvvJXKbx7RFwom5I1jb+BYfVxWXMmRfn8U91Lx9Pt2iq+\n" +
        "JmtjcQ2PlF5NmqtrUQJBAPa0Nz6Sut6d2D6JO/tv/UBPobh54/tzRCvr6VssL1Cj\n" +
        "anLSW84Yl4w67KvoBXR0JPLCgnJYqr8rXkB5vniPi+kCQQDcdACCP/7AVbhss0uJ\n" +
        "k4Vnk1yoQgsOJK/LGxtuKznzl8YMTyFME9nNOwEfKwcVaZJCzwiXIBbmTDCeEtgW\n" +
        "eSmjAkEAna1bbcvcUfJyxq1xv+e45oS+6ShGtWzbknLqqBIaf6CipZabhKMlIUR8\n" +
        "Bfd6nQ6qmtoFA851+09doznsqiOdGQJAdr+tvnuGWEhUbYku+U6Tn7VLRf89QUMV\n" +
        "xow1fGSgdMyei+bcAsT2n4xXEFj3GduiQ4aOFAnfC/KihaOU7pYZjQJAI50P1Up4\n" +
        "At2a3KYAmlb+MLymTwPlXaHZzXMfENSKaBd9OAwHvVl49XpX8K2aqDuqgQaw7HIn\n" +
        "MLxf7xYuV1GR2Q==\n" +
        "-----END PRIVATE KEY-----");
function encrypt(string){
    return crypt.encrypt(string)
}
function decrypt(string){
    return crypt.decrypt(string)
}