#include <jni.h>

#include <ctime>
#include <iostream>
#include <sstream>

#ifndef BASE64_H_C0CE2A47_D10E_42C9_A27C_C883944E704A
#define BASE64_H_C0CE2A47_D10E_42C9_A27C_C883944E704A

#include <string>

std::string base64Encode(unsigned char const *, unsigned int len);

#endif

static const std::string base64_chars =
        "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        "abcdefghijklmnopqrstuvwxyz"
        "0123456789+/";

std::string xorEncode(std::string data)
{
    char key[3] = {'K', 'C', 'Q'};
    std::string ret = data;

    for (int i = 0; i < data.size(); i++)
        ret[i] = data[i] ^ key[i % (sizeof(key) / sizeof(char))];

    return ret;
}

std::string base64Encode(unsigned char const *bytes_to_encode, unsigned int in_len)
{
    std::string ret;
    int i = 0;
    int j = 0;
    unsigned char char_array_3[3];
    unsigned char char_array_4[4];

    while (in_len--)
    {
        char_array_3[i++] = *(bytes_to_encode++);
        if (i == 3)
        {
            char_array_4[0] = (char_array_3[0] & 0xfc) >> 2;
            char_array_4[1] = ((char_array_3[0] & 0x03) << 4) + ((char_array_3[1] & 0xf0) >> 4);
            char_array_4[2] = ((char_array_3[1] & 0x0f) << 2) + ((char_array_3[2] & 0xc0) >> 6);
            char_array_4[3] = char_array_3[2] & 0x3f;

            for (i = 0; (i < 4); i++)
                ret += base64_chars[char_array_4[i]];
            i = 0;
        }
    }

    if (i)
    {
        for (j = i; j < 3; j++)
            char_array_3[j] = '\0';

        char_array_4[0] = (char_array_3[0] & 0xfc) >> 2;
        char_array_4[1] = ((char_array_3[0] & 0x03) << 4) + ((char_array_3[1] & 0xf0) >> 4);
        char_array_4[2] = ((char_array_3[1] & 0x0f) << 2) + ((char_array_3[2] & 0xc0) >> 6);

        for (j = 0; (j < i + 1); j++)
            ret += base64_chars[char_array_4[j]];

        while ((i++ < 3))
            ret += '=';
    }

    return ret;
}

std::string generateRAuth(std::string mac, std::string sn, std::string r_time)
{
    time_t epoch_time = time(nullptr);
    std::string delimiter = "|";
    std::string full_data = mac + delimiter + sn + delimiter + r_time;

    std::string xor_data = xorEncode(full_data);
    std::string ret = base64Encode(reinterpret_cast<const unsigned char *>(xor_data.c_str()), xor_data.length());

    return ret;
}


extern "C" JNIEXPORT jstring JNICALL
Java_com_redroid_iptv_MainActivity_generateRAuthJNI(JNIEnv *env, jobject, jstring MAC, jstring SN, jstring RTIME)
{
    const char *macstr = (env)->GetStringUTFChars(MAC, 0);
    const char *snstr = (env)->GetStringUTFChars(SN, 0);
    const char *rtimestr = (env)->GetStringUTFChars(RTIME, 0);

    std::string token = generateRAuth(macstr, snstr, rtimestr);
    return env->NewStringUTF(token.c_str());

    (env)->ReleaseStringUTFChars(MAC, macstr);
    (env)->ReleaseStringUTFChars(SN, snstr);
    (env)->ReleaseStringUTFChars(RTIME, rtimestr);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_redroid_iptv_repository_DeviceRepository_generateRAuthJNI(JNIEnv *env, jobject, jstring MAC, jstring SN, jstring RTIME)
{
    const char *macstr = (env)->GetStringUTFChars(MAC, 0);
    const char *snstr = (env)->GetStringUTFChars(SN, 0);
    const char *rtimestr = (env)->GetStringUTFChars(RTIME, 0);

    std::string token = generateRAuth(macstr, snstr, rtimestr);
    return env->NewStringUTF(token.c_str());

    (env)->ReleaseStringUTFChars(MAC, macstr);
    (env)->ReleaseStringUTFChars(SN, snstr);
    (env)->ReleaseStringUTFChars(RTIME, rtimestr);
}
