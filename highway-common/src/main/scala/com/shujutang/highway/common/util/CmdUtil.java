package com.shujutang.highway.common.util;

        import java.io.BufferedReader;
        import java.io.IOException;
        import java.io.InputStreamReader;

/**
 *
 *
 * 类描述：java执行[指令]
 * @since  jdk1.7
 * @version 1.0
 */
public class CmdUtil {


    /**
     * 执行外部命令，返回是否执行成功
     *
     * @param cmd
     * @return 返回结果
     */
    public static boolean exeCmdIsOK(String cmd) {

        boolean result = true;
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        } finally {
//            process.destroy();
        }
        return result;

    }

    /**
     * 执行外部命令，返回外部命令执行结果
     *
     * @param cmdStr
     *            外部命令的字符串
     * @return
     * 			返回外部命令执行结果,返回的字符串结果是按照UTF-8格式编码的。
     * 			如果返回的结果包含几行，则用"\n"分割。
     */
    public static String exeCmdResultUTF8(String cmdStr) {

        String result = null;
        StringBuffer strbuffer = null;
        Process process = null;
        BufferedReader bufferReader = null;
        try {
            process = Runtime.getRuntime().exec(cmdStr);//执行指令

            bufferReader = new BufferedReader(new InputStreamReader(process.getInputStream(),"UTF-8"));
            strbuffer = new StringBuffer();
            String line = null;
            while ((line = bufferReader.readLine()) != null) {
                strbuffer.append(line).append("\n");
            }

            result = strbuffer.toString();
            //错误信息
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream(), "UTF-8"));
            StringBuilder sbf = new StringBuilder();
            String lineError = null;
            while ((lineError = errorReader.readLine()) != null){
                sbf.append(lineError).append("\n");
            }
            System.out.println("错误信息【" + sbf.toString() + "】");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bufferReader != null){
                try {
                    bufferReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (process != null){
                process.destroy();
            }
        }
        return result;

    }

    /**
     * 执行外部命令并获取返回值
     *
     * @param cmd
     *            外部命令
     * @return 外部命令执行的返回值
     * @throws Exception
     *             执行中的异常
     */
    public static String cmd_result(String cmd) {
        Runtime rt = Runtime.getRuntime();
        String str[] = { "/bin/sh", "-c", cmd };
        Process pcs = null;
        BufferedReader br = null;
        String line = new String();
        StringBuffer buff = new StringBuffer();
        try {
            pcs = rt.exec(str);
            br = new BufferedReader(new InputStreamReader(pcs.getInputStream()));
            while ((line = br.readLine()) != null) {
                buff.append(line).append("\n");
            }
            pcs.waitFor();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (InterruptedException e) {
            System.err.println("processes was interrupted");
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (Exception e) {
            }
            if (pcs != null) {
                int ret = pcs.exitValue();
                pcs.destroy();
            }
        }
        return buff.toString();
    }

    public static void main(String[] args) {
        String result = CmdUtil.exeCmdResultUTF8("sh /topapp/topwalk/cpu.sh");
        System.out.println("sh /topapp/topwalk/cpu.sh========================"+result);
    }

}
