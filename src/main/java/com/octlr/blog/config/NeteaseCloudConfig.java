package com.octlr.blog.config;

public class NeteaseCloudConfig {

    // const text = JSON.stringify(object)
    //  const secretKey = crypto.randomBytes(16).map(n => (base62.charAt(n % 62).charCodeAt()))
    //  return {
    //    params: aesEncrypt(Buffer.from(aesEncrypt(Buffer.from(text), 'cbc', presetKey, iv).toString('base64')), 'cbc', secretKey, iv).toString('base64'),
    //    encSecKey: rsaEncrypt(secretKey.reverse(), publicKey).toString('hex')
    //  }
    public static final String  publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDgtQn2JZ34ZC28NWYpAUd98iZ37BUrX/aKzmFbt7clFSs6sXqHauqKWqdtLkF2KexO40H1YTX8z2lSgBBOAxLsvaklV8k4cBFK9snQXE9/DDaFt6Rr7iVZMldczhC0JNgTz+SHXT6CBHuX3e9SdB1Ua44oncaTWz7OBGLbCiK45wIDAQAB";
    public static final String  iv="0102030405060708";
    public static final String  presetKey="0CoJUm6Qyw8W8jud";

}
