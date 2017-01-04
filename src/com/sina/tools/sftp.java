package com.sina.tools;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.Vector;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
public class sftp {
    protected String host="222.73.39.37";//sftp服务器ip
    protected String username="200004595271";//用户名，即商户号
    protected String password;//密码，不使用，用的是密钥登录的方式
    protected String privateKey="C:\\data\\id_rsa";//密钥文件路径，联调环境密钥在网盘里key压缩包内
    protected String passphrase;//密钥口令
    protected int port = 50022;//默认的sftp端口号是22
    /**
     * 获取连接
     * @return channel
     */
    public ChannelSftp connectSFTP() {
        JSch jsch = new JSch();
        Channel channel = null;
        try {
            if (privateKey != null && !"".equals(privateKey)) {
                //使用密钥验证方式，密钥可以使有口令的密钥，也可以是没有口令的密钥
                if (passphrase != null && "".equals(passphrase)) {
                    jsch.addIdentity(privateKey, passphrase);
                } else {
                    jsch.addIdentity(privateKey);
                }
            }
            Session session = jsch.getSession(username, host, port);
            if (password != null && !"".equals(password)) {
                session.setPassword(password);
            }
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");// do not verify host key
            session.setConfig(sshConfig);
            // session.setTimeout(timeout);
            session.setServerAliveInterval(92000);
            session.connect();
            //参数sftp指明要打开的连接是sftp连接
            channel = session.openChannel("sftp");
            channel.connect();
            System.out.println("sftp连接成功");
        } catch (JSchException e) {
            e.printStackTrace();
        }
        return (ChannelSftp) channel;
    }
    
    /**
     * 上传文件
     * 
     * @param directory
     *            上传的目录
     * @param uploadFile
     *            要上传的文件
     * @param sftp
     */
    public void upload(String directory, String uploadFile, ChannelSftp sftp) {
        try {
            sftp.cd(directory);
            File file = new File(uploadFile);
            sftp.put(new FileInputStream(file), file.getName());
            System.out.println("上传完成!");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
/**
 * @throws SftpException 
 * 
 */
    public void ls(String directory,ChannelSftp sftp) throws SftpException
    {
    	sftp.cd(directory);
    	Vector v=sftp.ls("*.*");
        for(int i=0;i<v.size();i++)
        {
         System.out.print(v.get(i));
        }
    }
    /**
     * 下载文件
     * 
     * @param directory
     *            下载目录
     * @param downloadFile
     *            下载的文件
     * @param saveFile
     *            存在本地的路径
     * @param sftp
     */
    public void download(String directory, String downloadFile,
            String saveFile, ChannelSftp sftp) {
        try {
            sftp.cd(directory);
            sftp.get(downloadFile,saveFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除文件
     * 
     * @param directory
     *            要删除文件所在目录
     * @param deleteFile
     *            要删除的文件
     * @param sftp
     */
    public void delete(String directory, String deleteFile, ChannelSftp sftp) {
        try {
            sftp.cd(directory);
            sftp.rm(deleteFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void disconnected(ChannelSftp sftp){
        if (sftp != null) {
            try {
                sftp.getSession().disconnect();
            } catch (JSchException e) {
                e.printStackTrace();
            }
            sftp.disconnect();
        }
    }
    //测试sftp连接
    public static void main(String args[]) throws SftpException 
    { 
    	System.out.println("连接sftp");
    	sftp s=new sftp();
    	//s.upload("/upload/", "C:\\data\\4100.zip",s.connectSFTP());
    	 s.ls("/upload/",s.connectSFTP());
    } 
    
}