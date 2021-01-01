//
// Created by Cahya Nugraha on 07/12/2020.
//

#include <jni.h>
#include <android/log.h>

extern "C" jint Java_id_ac_ui_cs_mobileprogramming_pandeketutcahyanugraha_warcraftinfo_calculator_ui_CalculatorActivity_nativeAdd(
        JNIEnv* env,
        jobject /* this */,
        int first,
        int second
        ) {
        __android_log_print(
                ANDROID_LOG_INFO,
                "native_math_nativeAdd",
                "Adding: %d + %d = %d", first, second, first + second
        );
        return first + second;
}

extern "C" jint Java_id_ac_ui_cs_mobileprogramming_pandeketutcahyanugraha_warcraftinfo_calculator_ui_CalculatorActivity_nativeSubtract(
        JNIEnv* env,
        jobject /* this */,
        int first,
        int second
        ) {
        __android_log_print(
                ANDROID_LOG_INFO,
                "native_math_nativeSubtract",
                "Subtracting: %d - %d = %d", first, second, first - second
        );
        return first - second;
}