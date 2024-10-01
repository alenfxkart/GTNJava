import java.security.KeyFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;


public class GTNApiCall {

    public String DecryptionHandler(final String institutionId, final String messageType, final String messageID, final String parameters) {
//        try {
//            OkHttpClient client = new OkHttpClient().newBuilder()
//                    .build();
//            MediaType mediaType = MediaType.parse("application/json");
//            RequestBody body = RequestBody.create(mediaType, "{ \r\n \"institutionId\": \"138\", \r\n \"messageType\": \"41\", \r\n \"messageID\": \"680\", \r\n \"parameters\": \"VQXsPpp8WtNY+N1tE3wLG4hWwrM3klriL3ncDWIxD6k4dYpfGySgRTUcYACZpeAysrl0/qzQiEbnmm9eVhH5J4RA/yOhgaiUqR5QDuXVEloEs6jqC9oa6bLf0DizV9iJAaecmhykkJzO7voXpKxIsLIOES75oH1PX9tbgS2/xCFXuI/lkPR8w4R/FKRgUfiB0uPe9byNaHS4iPNFJUESRGzGoYckWC/E+xE3F+lXnt0Shzr0SNTNcUSN/I44MNhTpxbx1pyGHNKPkJKIYkh674g2WZnR4VG0gtrPbR9DZP/wlCsN3DSI9APM7jj6HdJK7SnnYU/T5O1pqGWiDqup1Q==\" \r\n}\r\n");
//            Request request = new Request.Builder()
//                    .url("https://service-uat.globaltradingnetwork.com/MubasherWebServices/AUB")
//                    .method("POST", body)
//                    .addHeader("Content-Type", "application/json")
//                    .addHeader("Cookie", "Path=/")
//                    .build();
//            Response response = client.newCall(request).execute();
//
////            Unirest.setTimeouts(0, 0);
////            HttpResponse<String> response = Unirest.post("https://service-uat.globaltradingnetwork.com/MubasherWebServices/AUB")
////                    .header("Content-Type", "application/json")
////                    .header("Cookie", "Path=/")
////                    .body("{ \r\n \"institutionId\": \"138\", \r\n \"messageType\": \"41\", \r\n \"messageID\": \"680\", \r\n \"parameters\": \"VQXsPpp8WtNY+N1tE3wLG4hWwrM3klriL3ncDWIxD6k4dYpfGySgRTUcYACZpeAysrl0/qzQiEbnmm9eVhH5J4RA/yOhgaiUqR5QDuXVEloEs6jqC9oa6bLf0DizV9iJAaecmhykkJzO7voXpKxIsLIOES75oH1PX9tbgS2/xCFXuI/lkPR8w4R/FKRgUfiB0uPe9byNaHS4iPNFJUESRGzGoYckWC/E+xE3F+lXnt0Shzr0SNTNcUSN/I44MNhTpxbx1pyGHNKPkJKIYkh674g2WZnR4VG0gtrPbR9DZP/wlCsN3DSI9APM7jj6HdJK7SnnYU/T5O1pqGWiDqup1Q==\" \r\n}\r\n")
////                    .asString();
//
//
//            System.out.println("Response==" + response);
//        } catch (Exception e) {
//            // logger.error("SSO Security Facade Exception");
//        }
//    }

        return "";
    }
}
