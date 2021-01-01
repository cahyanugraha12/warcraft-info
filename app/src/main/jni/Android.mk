LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := native_math
LOCAL_SRC_FILES := native_math.cpp
LOCAL_LDLIBS := -llog

include $(BUILD_SHARED_LIBRARY)