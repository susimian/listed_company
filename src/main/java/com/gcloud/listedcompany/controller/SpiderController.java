package com.gcloud.listedcompany.controller;

import com.gcloud.listedcompany.dao.CompanyDao;
import com.gcloud.listedcompany.pojo.Company;
import com.gcloud.listedcompany.util.HttpUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
public class SpiderController {

    @Autowired
    private CompanyDao companyDao;

    public void run() {
        //xsb();
        //gg();
        //mg();
        gg2();
    }

    public void xsb() {
        try {
            int totalPage = 1;
            for (int i = 1; i <= totalPage; i++) {
                String url = "http://xinsanban.eastmoney.com/api/QuoteCenter/GPGS/GetGPGS?level=1&page=" + i + "&pagesize=20&sortType=ChangePercent&sortRule=-1";
                String res = HttpUtil.httpGet(url, null);
                JsonObject jsonObject = new JsonParser().parse(res).getAsJsonObject();
                totalPage = jsonObject.get("TotalPage").getAsInt();

                JsonArray projectSupplies = jsonObject.get("result").getAsJsonArray();
                for (JsonElement object : projectSupplies) {
                    String code = object.getAsJsonObject().get("Code").getAsString();

                    String companyUrl = "http://xinsanban.eastmoney.com/F10/CompanyInfo/Introduction/" + code + ".html";
                    Document page = Jsoup.connect(companyUrl).get();
                    Elements detail = page.select("#company_info");
                    Elements lis = detail.select("li");

                    String name = lis.get(0).select("span").get(1).text();
                    String phone = lis.get(4).select("span").get(1).text();
                    String business = lis.get(7).select("span").get(1).text();
                    String chairman = lis.get(3).select("span").get(1).text();
                    String count = lis.get(14).select("span").get(1).text();
                    String fax = lis.get(16).select("span").get(1).text();
                    String introduce = lis.get(11).select("span").get(1).text();
                    String industry = lis.get(8).select("span").get(1).text();
                    String website = lis.get(17).select("a").text();
                    String companyCode = code;
                    String area = lis.get(10).select("span").get(1).text();
                    String type = "新三板上市公司";

                    Company company = new Company(null, companyCode, name, area, industry, website, business, chairman, count, phone, fax, introduce, type);
                    companyDao.save(company);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void gg() {
        try {
            int i = 1;
            String url = "http://vip.stock.finance.sina.com.cn/quotes_service/api/json_v2.php/Market_Center.getHKStockData?page=" + i + "&num=40&sort=symbol&asc=1&node=qbgg_hk&_s_r_a=page";
            String res = HttpUtil.httpGet(url, null);
            while (!"null".equals(res)) {
                JsonArray projectSupplies = new JsonParser().parse(res).getAsJsonArray();
                for (JsonElement object : projectSupplies) {
                    String code = object.getAsJsonObject().get("symbol").getAsString();

                    String companyUrl = "http://stock.finance.sina.com.cn/hkstock/info/" + code + ".html";
                    Document page = Jsoup.connect(companyUrl).get();
                    Elements detail = page.select("#sub01_c1");
                    Elements trs = detail.select("tr");

                    String name = trs.get(1).select("td").get(1).text();
                    String phone = trs.get(18).select("td").get(1).text();
                    String business = "";
                    String chairman = trs.get(6).select("td").get(1).text();
                    String count = "";
                    String fax = trs.get(19).select("td").get(1).text();
                    String introduce = trs.get(3).select("td").get(1).text();
                    String industry = trs.get(4).select("td").get(1).text();
                    String website = trs.get(16).select("td").get(1).text();
                    String companyCode = code;
                    String area = trs.get(11).select("td").get(1).text();
                    String type = "香港上市公司";

                    Company company = new Company(null, companyCode, name, area, industry, website, business, chairman, count, phone, fax, introduce, type);
                    companyDao.save(company);

                }
                i++;
                res = HttpUtil.httpGet(url, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void mg() {
        try {
            Map<String, String> cookies = null;
            cookies = Jsoup.connect("https://xueqiu.com/hq#exchange=US&industry=3_3&firstName=3").execute().cookies();

            int i = 1;
            String url = "https://xueqiu.com/service/v5/stock/screener/quote/list?page="+i+"&size=30&order=desc&orderby=percent&order_by=percent&market=US&type=us_china&_=1570758878324";
            String res = HttpUtil.httpGet(url, null);
            JsonObject jsonObject = new JsonParser().parse(res).getAsJsonObject();
            int size = jsonObject.get("data").getAsJsonObject().get("list").getAsJsonArray().size();

            while (size != 0) {
                JsonArray projectSupplies = jsonObject.get("data").getAsJsonObject().get("list").getAsJsonArray();
                for (JsonElement object : projectSupplies) {
                    String code = object.getAsJsonObject().get("symbol").getAsString();

                    String companyUrl = "https://stock.xueqiu.com/v5/stock/f10/us/company.json?symbol=" + code ;
                    Connection.Response page = Jsoup.connect(companyUrl).header("Cookie",cookies.toString()).ignoreContentType(true).execute();
                    String companyStr = page.body();
                    JsonObject companyObject = new JsonParser().parse(companyStr).getAsJsonObject().get("data").getAsJsonObject().get("company").getAsJsonObject();

                    String name = companyObject.get("org_name_cn").getAsString();
                    String phone = companyObject.get("telephone").getAsString();
                    String business = companyObject.get("main_operation_business").getAsString();
                    String chairman = companyObject.get("chairman").getAsString();
                    String count = companyObject.get("staff_num").getAsString();
                    String fax = companyObject.get("fax").getAsString();
                    String introduce = companyObject.get("org_cn_introduction").getAsString();
                    String industry = "";
                    String website = companyObject.get("org_website").getAsString();
                    String companyCode = code;
                    String area = companyObject.get("office_address_cn").getAsString();
                    String type = "美国上市公司";

                    Company company = new Company(null, companyCode, name, area, industry, website, business, chairman, count, phone, fax, introduce, type);
                    companyDao.save(company);
                }
                i++;
                res = HttpUtil.httpGet(url, null);
                jsonObject = new JsonParser().parse(res).getAsJsonObject();
                size = jsonObject.get("data").getAsJsonObject().get("list").getAsJsonArray().size();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void gg2() {
        try {
            Map<String, String> cookies = null;
            cookies = Jsoup.connect("https://xueqiu.com/hq#exchange=US&industry=3_3&firstName=3").execute().cookies();

            int i = 1;
            String url = "https://xueqiu.com/service/v5/stock/screener/quote/list?page="+i+"&size=30&order=desc&orderby=percent&order_by=percent&market=HK&type=hk&is_delay=true&_=1571101235562";
            String res = HttpUtil.httpGet(url, null);
            JsonObject jsonObject = new JsonParser().parse(res).getAsJsonObject();
            int size = jsonObject.get("data").getAsJsonObject().get("list").getAsJsonArray().size();

            while (size != 0) {
                JsonArray projectSupplies = jsonObject.get("data").getAsJsonObject().get("list").getAsJsonArray();
                for (JsonElement object : projectSupplies) {
                    String code = object.getAsJsonObject().get("symbol").getAsString();

                    String companyUrl = "https://stock.xueqiu.com/v5/stock/f10/hk/company.json?symbol=" + code ;
                    Connection.Response page = Jsoup.connect(companyUrl).header("Cookie",cookies.toString()).ignoreContentType(true).execute();
                    String companyStr = page.body();
                    JsonObject companyObject = new JsonParser().parse(companyStr).getAsJsonObject().get("data").getAsJsonObject().get("company").getAsJsonObject();

                    String name = companyObject.get("comcnname").getAsString();
                    String phone = companyObject.get("tel").isJsonNull()?"":companyObject.get("tel").getAsString();
                    String business = companyObject.get("mbu").isJsonNull()?"":companyObject.get("mbu").getAsString();
                    String chairman = companyObject.get("chairman").isJsonNull()?"":companyObject.get("chairman").getAsString();
                    String count = "";
                    String fax = companyObject.get("fax").isJsonNull()?"":companyObject.get("fax").getAsString();
                    String introduce = companyObject.get("comintr").isJsonNull()?"":companyObject.get("comintr").getAsString();
                    String industry = business;
                    String website = companyObject.get("webSite").isJsonNull()?"":companyObject.get("webSite").getAsString();
                    String companyCode = code;
                    String area = companyObject.get("nationName").isJsonNull()?"":companyObject.get("nationName").getAsString();
                    String type = "香港上市公司";

                    Company company = new Company(null, companyCode, name, area, industry, website, business, chairman, count, phone, fax, introduce, type);
                    //companyDao.save(company);

                    // 获取财报
                    String reportUrl = "https://stock.xueqiu.com/v5/stock/finance/hk/income.json?symbol="+code+"&type=all&is_detail=true&count=5&timestamp="+System.currentTimeMillis();
                    Connection.Response report = Jsoup.connect(reportUrl).header("Cookie",cookies.toString()).ignoreContentType(true).execute();
                    String reportStr = report.body();
                    JsonArray reportArray = new JsonParser().parse(reportStr).getAsJsonObject().get("data").getAsJsonObject().get("list").getAsJsonArray();


                    for (JsonElement element:reportArray){
                        System.out.println(element);

                        String currency = new JsonParser().parse(reportStr).getAsJsonObject().get("currency_name").getAsString();
                        String report_name = element.getAsJsonObject().get("report_name").isJsonNull()?"":element.getAsJsonObject().get("report_name").getAsString();
                        String zyye = element.getAsJsonObject().get("tto").isJsonNull()?"":element.getAsJsonObject().get("tto").getAsJsonArray().get(0).toString();

                        String cb = element.getAsJsonObject().get("slgcost").isJsonNull()?"":element.getAsJsonObject().get("slgcost").getAsString();
                        String lr = element.getAsJsonObject().get("gp").isJsonNull()?"":element.getAsJsonObject().get("gp").getAsJsonArray().get(0).toString();

                        //String zyye = element.getAsJsonObject().get("tto").isJsonNull()?"":element.getAsJsonObject().get("tto").getAsJsonArray().get(0).toString();
                        /*String chairman = companyObject.get("chairman").isJsonNull()?"":companyObject.get("chairman").getAsString();
                        String count = "";
                        String fax = companyObject.get("fax").isJsonNull()?"":companyObject.get("fax").getAsString();
                        String introduce = companyObject.get("comintr").isJsonNull()?"":companyObject.get("comintr").getAsString();
                        String industry = business;
                        String website = companyObject.get("webSite").isJsonNull()?"":companyObject.get("webSite").getAsString();
                        String companyCode = code;
                        String area = companyObject.get("nationName").isJsonNull()?"":companyObject.get("nationName").getAsString();
                        String type = "香港上市公司";*/
                        System.out.println(report_name);
                    }

                }
                i++;
                res = HttpUtil.httpGet(url, null);
                jsonObject = new JsonParser().parse(res).getAsJsonObject();
                size = jsonObject.get("data").getAsJsonObject().get("list").getAsJsonArray().size();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
