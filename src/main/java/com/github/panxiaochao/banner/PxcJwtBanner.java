package com.github.panxiaochao.banner;

import com.github.panxiaochao.PxcVersion;

/**
 * @author Mr_LyPxc
 * @title: PxcJwtBanner
 * @description: 启动图案
 * @date 2021/12/18 19:23
 */
public class PxcJwtBanner {
    private static final String PXC_JWT = ":: Pxc-Jwt ::";

    /**
     * 打印banner
     */
    public void printBanner() {
        String version = PxcVersion.getVersion();
        System.out.println(bannerInfo(version));
    }

    /**
     * 生成字符串网址：https://www.bootschool.net/ascii <br/>
     * 字体选择：stick-letters
     *
     * @return
     */
    private String bannerInfo(String version) {
        StringBuilder textBuilder = new StringBuilder();
        textBuilder.append(" __       __               ___ \n");
        textBuilder.append("|__) \\_/ /  ` __    | |  |  |  \n");
        textBuilder.append("|    / \\ \\__,    \\__/ |/\\|  |  \n");

        version = version != null ? " (v" + version + ")" : "";
        int strap_line_size = (textBuilder.length() / 3) - (PXC_JWT.length() + version.length()) - 2;

        textBuilder.append(PXC_JWT);
        for (int i = 0; i < strap_line_size; i++) {
            textBuilder.append(" ");
        }
        textBuilder.append(version);
        textBuilder.append("\n");
        return textBuilder.toString();
    }
}
