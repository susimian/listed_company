package com.gcloud.listedcompany.controller;

import com.gcloud.listedcompany.dao.CompanyDao;
import com.gcloud.listedcompany.dao.HSCompanyDao;
import com.gcloud.listedcompany.dao.JobDao;
import com.gcloud.listedcompany.pojo.HSCompany;
import com.gcloud.listedcompany.pojo.Job;
import com.gcloud.listedcompany.util.HttpUtil;
import com.gcloud.listedcompany.util.MD5;
import com.gcloud.listedcompany.util.Util;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
public class Spider {
    @Autowired
    private CompanyDao companyDao;
    @Autowired
    private HSCompanyDao hsCompanyDao;
    @Autowired
    private JobDao jobDao;

    public void run(){
        //HS();
        worker();
    }

    public void HS(){
        String type = "沪深上市公司";
        int i = 1;
        String url = "https://xueqiu.com/service/v5/stock/screener/quote/list?page="+i+"&size=30&order=desc&orderby=percent&order_by=percent&market=CN&type=sh_sz&_=1572508947437";
        try {
            Map<String, String> cookies = null;
            cookies = Jsoup.connect("https://xueqiu.com/hq#exchange=US&industry=3_3&firstName=3").execute().cookies();

            String res = HttpUtil.httpGet(url, null);
            JsonObject jsonObject = new JsonParser().parse(res).getAsJsonObject();
            int size = jsonObject.get("data").getAsJsonObject().get("list").getAsJsonArray().size();

            while (size != 0) {
                JsonArray projectSupplies = jsonObject.get("data").getAsJsonObject().get("list").getAsJsonArray();
                for (JsonElement object : projectSupplies) {
                    String code = object.getAsJsonObject().get("symbol").getAsString();

                    String companyUrl = "https://stock.xueqiu.com/v5/stock/f10/cn/company.json?symbol=" + code ;
                    Connection.Response page = Jsoup.connect(companyUrl).header("Cookie",cookies.toString()).ignoreContentType(true).execute();
                    String companyStr = page.body();
                    JsonObject companyObject = new JsonParser().parse(companyStr).getAsJsonObject().get("data").getAsJsonObject().get("company").getAsJsonObject();

                    String classi_name = companyObject.get("classi_name").getAsString();


                    HSCompany company = new HSCompany();
                    company.setOwnership(classi_name);
                    hsCompanyDao.save(company);
                }
                i=i+1;
                res = HttpUtil.httpGet("https://xueqiu.com/service/v5/stock/screener/quote/list?page="+i+"&size=30&order=desc&orderby=percent&order_by=percent&market=CN&type=sh_sz&_=1572508947437", null);
                jsonObject = new JsonParser().parse(res).getAsJsonObject();
                size = jsonObject.get("data").getAsJsonObject().get("list").getAsJsonArray().size();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void worker(){
        int i=1;
        int total = 1;

        for (i=1;i<=total;i++) {
            String url = "http://job.dgut.edu.cn/job/website/recruitApplyList?pageNo="+i+"&pageSize=50&bigMeetingRecruitId=0a134a055c7946929a634742e2b6bab7&postid";
            String res = HttpUtil.httpGet(url, null);
            JsonObject jsonObject = new JsonParser().parse(res).getAsJsonObject();
            total = jsonObject.get("totalPage").getAsInt();

            JsonArray array = jsonObject.get("data").getAsJsonArray();
            for (JsonElement element : array) {
                JsonObject company = element.getAsJsonObject();
                String name = company.get("entName").getAsString();
                String introduce = company.get("entIntroduction").getAsString();
                String website = company.get("entHomepage").getAsString();
                String address = company.get("entAddress").getAsString();
                String fqh = "";
                String twh = "";
                String email = company.get("entEmail").getAsString();
                JsonArray jobArray = company.get("jobRequirements").getAsJsonArray();
                for (JsonElement jobElement : jobArray) {
                    JsonObject job = jobElement.getAsJsonObject();

                    String position = job.get("post").getAsString();
                    String area = job.get("jobArea").getAsString();
                    String number = job.get("requireNum").getAsString();
                    String speciality = job.get("speRequirement").getAsString();
                    String education = job.get("eduLevel").getAsString();
                    String salary = job.get("salary").getAsString();
                    String require = job.get("jobRequire").getAsString();

                    Job jobSave = new Job(null,name,introduce,website,address,fqh,twh,
                            email,position,area,number,speciality,education,salary, require,null);
                    save(jobSave);

                }

            }
            Util.sleep(1,2);
        }

    }


    private void save(Job job) {
        String key = MD5.md5(job.getCompany()+job.getPosition()+job.getUniKey());
        if (jobDao.countByUniKey(key) == 0) {
            job.setUniKey(key);
            jobDao.save(job);
            System.out.println("新增 "+job.getCompany()+" : "+job.getPosition());
        }else {
            System.out.println("跳过");
        }
    }
}


