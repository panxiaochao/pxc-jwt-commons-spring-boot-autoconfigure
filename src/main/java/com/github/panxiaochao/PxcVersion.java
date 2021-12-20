package com.github.panxiaochao;

/**
 * @author Mr_LyPxc
 * @title: PxcVersion
 * @description: 获取版本号
 * @date 2021/12/19 11:28
 */
public class PxcVersion {
    private PxcVersion() {
    }

    public static String getVersion() {
        return determinePxcVersion();
    }

    /**
     * 返回版本信息
     *
     * @return
     */
    private static String determinePxcVersion() {
        Package pkg = PxcVersion.class.getPackage();
        return (pkg != null ? pkg.getImplementationVersion() : "null");
    }
}
