package kr.ontherec.api.infra.model;

public class Regex {
    public static final String BANK_ACCOUNT = "\\d{8,14}"; // https://www.cmsedi.or.kr/cms/board/workdata/view/992
    public static final String BUSINESS_REGISTRATION_NUMBER = "\\d{10}";
    public static final String ZIP_CODE = "\\d{5}";
}
