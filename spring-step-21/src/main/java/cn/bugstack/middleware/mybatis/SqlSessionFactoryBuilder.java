package cn.bugstack.middleware.mybatis;

import cn.hutool.core.lang.hash.Hash;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqlSessionFactoryBuilder {

    /**
     * 解析 配置xml 与 sql xml，初始化数据库链接。
     * 初始化 DefaultSqlSessionFactory
     * @param reader
     * @return
     */
    public DefaultSqlSessionFactory build(InputStream inputStream) {
        SAXReader saxReader = new SAXReader();
        try {
//            Document document = saxReader.read(new InputSource(reader));
            Document document = saxReader.read(inputStream);
            Configuration configuration = parseConfiguration(document.getRootElement());
            return new DefaultSqlSessionFactory(configuration);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }


    private Configuration parseConfiguration(Element root) {
        Configuration configuration = new Configuration();
        // 从xml文件中解析出数据源配置
        configuration.setDataSource(dataSource(root.element("environments").element("environment").element("dataSource")));
        // 创建数据库链接
        configuration.setConnection(connection(configuration.dataSource));
        // 从配置文件中解析出mapper文件路径，并解析mapper文件，获取sql语句信息
        configuration.setMapperElement(mapperElement(root.element("mappers")));
        return configuration;
    }

    private Map<String, String> dataSource(Element element) {
        HashMap<String, String> dataSource = new HashMap<>(4);
        List<Element> propertyList = element.elements("property");
        for (Element e : propertyList) {
            String name = e.attributeValue("name");
            String value = e.attributeValue("value");
            dataSource.put(name, value);
        }
        return dataSource;
    }

    private Connection connection(Map<String, String> dataSource) {
        try {
            Class.forName(dataSource.get("driver"));
            return DriverManager.getConnection(dataSource.get("url"), dataSource.get("username"), dataSource.get("password"));
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 获取SQL语句信息
    private Map<String, XNode> mapperElement(Element mappers) {
        Map<String, XNode> map = new HashMap<>();

        // 解析到多个mapper文件路径
        List<Element> mapperList = mappers.elements("mapper");
        for (Element e : mapperList) {
            String resource = e.attributeValue("resource");
            try {
                // 读取mapper文件
                Reader reader = Resources.getResourceAsReader(resource);
                SAXReader saxReader = new SAXReader();
                Document document = saxReader.read(new InputSource(reader));
                Element root = document.getRootElement();

                // 命名空间, 接口地址，namespace="cn.bugstack.middleware.mybatis.test.dao.IUserDao"
                String namespace = root.attributeValue("namespace");

                // SELECT
                List<Element> selectNodes = root.elements("select");
                for (Element node : selectNodes) {
                    String id = node.attributeValue("id");
                    String parameterType = node.attributeValue("parameterType");
                    String resultType = node.attributeValue("resultType");
                    String sql = node.getText();

                    // ? 匹配, key:参数序号，value：参数内容
                    Map<Integer, String> parameter = new HashMap<>();
                    Pattern pattern = Pattern.compile("(#\\{(.*?)})");
                    Matcher matcher = pattern.matcher(sql);
                    for (int i = 1; matcher.find(); i++) {
                        String g1 = matcher.group(1);
                        String g2 = matcher.group(2);
                        parameter.put(i, g2);
                        sql = sql.replace(g1, "?");
                    }

                    XNode xNode = new XNode();
                    xNode.setNamespace(namespace);
                    xNode.setId(id);
                    xNode.setParameterType(parameterType);
                    xNode.setResultType(resultType);
                    xNode.setSql(sql);
                    xNode.setParameter(parameter);

                    map.put(namespace + "." + id, xNode);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return map;
    }
}
