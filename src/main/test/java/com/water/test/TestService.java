package com.water.test;

import com.water.crawl.utils.http.HttpRequestTool;
import com.water.crawl.utils.lang.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangmiaojie on 2017/4/13.
 */
public class TestService {

    public static void main(String[] args) throws UnsupportedEncodingException {
        String url = "http://www.shengyeji.com/gong/wenzhang.php";
        Map<String, String> paramMap = new HashMap<>();
        String nei   = "什么是RubyGems呢？\n" +
                "\n" +
                "RubyGems是一个方便强大的Ruby程序包管理容器，Ruby的第三方的插件是用gem方式来管理的，非常容易发布和共享，一个简单的命令就可以安装上第三方的扩展库。特点：能远程安装包，包与包之间依赖关系的管理，简单可靠的卸载，查询机制，可以查询本地和远程服务器的包信息，可以保持一个包的不同版本，基于Web的查看接口，可以查看你安装的gem的信息。\n" +
                "\n" +
                "安装RubyGems\n" +
                "\n" +
                "\n" +
                "下载地址：http://rubyforge.org/frs/?group_id=126\n" +
                "\n" +
                "Windows 用户可以直接下载.zip压缩包，解压缩后从CMD提示窗口下进入 setup.rb所在的目录， 运行 ruby setup.rb 即可安装。\n" +
                "\n" +
                "Ruby1.9.1 以后的版本自带RubyGems，直接在CMD窗口下输入指令 gem update --system 升级到最新版即可。\n" +
                "\n" +
                "gem包的安装方式：\n" +
                "\n" +
                "RubyGems.org 是官方的Gem托管中心，RubyGems就是从这里远程下载gem包的。RubyGems 将所有的gem包 安装到 /[ruby root]/lib/ruby/gems/[ver]/ 目录下，这其中包括了cache、doc、gems、specifications 4个目录，cache下放置下载的原生gem包，gems下则放置的是解压过的gem包。当安装过程中遇到问题时，可以进入这些目录，手动删除有问题的gem包，然后重新运行 gem install [gemname] 命令即可。\n" +
                "\n" +
                "RubyGems命令详解：\n" +
                "\n" +
                "我们以目前最新的 rubygems 1.8.16 为例：\n" +
                "\n" +
                "# 查看RubyGems软件的版本\n" +
                "gem -v\n" +
                "\n" +
                "# 更新升级RubyGems软件自身\n" +
                "gem update --system\n" +
                "\n" +
                "# 更新所有已安装的gem包\n" +
                "$ gem update\n" +
                "\n" +
                "# 更新指定的gem包\n" +
                "# 注意：gem update [gemname]不会升级旧版本的包，可以使用 gem install [gemname] --version=[ver]代替\n" +
                "$ gem update [gemname]\n" +
                "\n" +
                "# 安装指定gem包，程序先从本机查找gem包并安装，如果本地没有，则从远程gem安装。\n" +
                "gem install [gemname]\n" +
                "\n" +
                "# 仅从本机安装gem包\n" +
                "gem install -l [gemname]\n" +
                "\n" +
                "# 仅从远程安装gem包\n" +
                "gem install -r [gemname]\n" +
                "\n" +
                "# 安装gem包，但不安装相关文档文件\n" +
                "gem install [gemname] --no-ri --no-rdoc\n" +
                "\n" +
                "# 安装指定版本的gem包\n" +
                "gem install [gemname] --version=[ver]\n" +
                "\n" +
                "# 删除指定的gem包，注意此命令将删除所有已安装的版本\n" +
                "gem uninstall [gemname]\n" +
                "\n" +
                "# 删除某指定版本gem\n" +
                "gem uninstall [gemname] --version=[ver]\n" +
                "\n" +
                "# 查看本机已安装的所有gem包\n" +
                "gem list\n" +
                "\n" +
                "# 列出远程RubyGems.org 上有此关键字的gem包（可用正则表达式）\n" +
                "gem list -r keyword\n" +
                "\n" +
                "# 列出远程RubyGems.org 上所有Gmes清单，并保存到文件。\n" +
                "gem list -r > remote_gem_list.txt\n" +
                "\n" +
                "#查看所有gem包文档及资料\n" +
                "gem server \n" +
                "\n" +
                "#显示RubyGem使用帮助\n" +
                "gem help\n" +
                "\n" +
                "#列出RubyGem命令一些使用范例\n" +
                "gem help example\n" +
                "\n";
        System.out.println(nei);
        String cha = "检测";
        paramMap.put("nei", StringUtil.encode(nei, "gb2312"));
        paramMap.put("cha", StringUtil.encode(cha, "gb2312"));
        String content = (String) HttpRequestTool.postRequest(url, paramMap, false);
//        content = new String(content.getBytes("UTF-8"),"ISO-8859-1");
//        System.out.println(content);
        Document doc = Jsoup.parse(content);
        Element ele  = doc.select(".query_box").get(0);
        String result = ele.ownText();
        System.out.println(result);
    }

    public static String getUTF8StringFromGBKString(String gbkStr) {
        try {
            return new String(getUTF8BytesFromGBKString(gbkStr), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new InternalError();
        }
    }

    public static byte[] getUTF8BytesFromGBKString(String gbkStr) {
        int n = gbkStr.length();
        byte[] utfBytes = new byte[3 * n];
        int k = 0;
        for (int i = 0; i < n; i++) {
            int m = gbkStr.charAt(i);
            if (m < 128 && m >= 0) {
                utfBytes[k++] = (byte) m;
                continue;
            }
            utfBytes[k++] = (byte) (0xe0 | (m >> 12));
            utfBytes[k++] = (byte) (0x80 | ((m >> 6) & 0x3f));
            utfBytes[k++] = (byte) (0x80 | (m & 0x3f));
        }
        if (k < utfBytes.length) {
            byte[] tmp = new byte[k];
            System.arraycopy(utfBytes, 0, tmp, 0, k);
            return tmp;
        }
        return utfBytes;
    }
}
