/**
 * -----------------------------------------------------------------------------
 * Copyright © 2015 DtDream Science and Technology Co.,Ltd. All rights reserved.
 * -----------------------------------------------------------------------------
 * Product:
 * Module Name:
 * Date Created: 2016/12/8
 * Description:
 * -----------------------------------------------------------------------------
 * Modification History
 * DATE            Name           Description
 * -----------------------------------------------------------------------------
 * 2016/12/8      thomugo
 * -----------------------------------------------------------------------------
 */

package com.dtdream.cli.util;

/**
 * Description 控制台输出格式化工具
 * @author thomugo
 * @since 1.0.0
 * @date 2016/12/8
 */
public final class FormatUtil {

    /**
     * Description: 将jsonStr打印到控制台
     *
     * @param jsonStr
     * @author thomugo
     * @since V1.0.0
     * @date 2016/12/8
     */
    public static void printJson(String jsonStr){
        System.out.println(formatJson(jsonStr));
    }

    /**
     * Description: 格式化jsonStr
     *
     * @param jsonStr
     * @return String
     * @author thomugo
     * @since V1.0.0
     * @date 2016/12/8
     */
    public static String formatJson(String jsonStr) {
        if (null == jsonStr || "".equals(jsonStr)) return "";
        StringBuilder sb = new StringBuilder();
        char last = '\0';
        char current = '\0';
        int indent = 0;
        for (int i = 0; i < jsonStr.length(); i++) {
            last = current;
            current = jsonStr.charAt(i);
            switch (current) {
                case '{':
                case '[':
                    sb.append(current);
                    sb.append('\n');
                    indent++;
                    addIndentBlank(sb, indent);
                    break;
                case '}':
                case ']':
                    sb.append('\n');
                    indent--;
                    addIndentBlank(sb, indent);
                    sb.append(current);
                    break;
                case ',':
                    sb.append(current);
                    if (last != '\\') {
                        sb.append('\n');
                        addIndentBlank(sb, indent);
                    }
                    break;
                default:
                    sb.append(current);
            }
        }

        return sb.toString();
    }

    /**
     * 添加space
     * @param sb
     * @param indent
     * @author   lizhgb
     * @Date   2015-10-14 上午10:38:04
     */
    /**
     * Description: 添加制表符
     *
     * @param sb StringBuilder
     * @param count 添加的数量
     * @author thomugo
     * @since V1.0.0
     * @date 2016/12/8
     */
    private static void addIndentBlank(StringBuilder sb, int count) {
        for (int i = 0; i < count; i++) {
            sb.append('\t');
        }
    }
}
