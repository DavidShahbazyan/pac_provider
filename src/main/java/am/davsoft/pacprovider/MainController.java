package am.davsoft.pacprovider;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class MainController {

    private static final String TYPE_DIRECT = "DIRECT";
    private static final String TYPE_PROXY = "PROXY";
    private static final String TYPE_SOCKS = "SOCKS";
    private static final String TYPE_HTTP = "HTTP";
    private static final String TYPE_HTTPS = "HTTPS";
    private static final String TYPE_SOCKS4 = "SOCKS4";
    private static final String TYPE_SOCKS5 = "SOCKS5";

    private static final String help = "<h1>Dynamic PAC [Proxy Auto Configuration] File Provider</h1>" +
            "<hr/>" +
            "<h2>Available generation methods:</h2>" +
            "<ul>" +
            "<li>/getDirect</li>" +
            "<li>/getProxy?host=&lt;HOST_ADDRESS&gt;&amp;port=&lt;PORT_NUMBER&gt;</li>" +
            "<li>/getSocks?host=&lt;HOST_ADDRESS&gt;&amp;port=&lt;PORT_NUMBER&gt;</li>" +
            "<li>/getHttp?host=&lt;HOST_ADDRESS&gt;&amp;port=&lt;PORT_NUMBER&gt;</li>" +
            "<li>/getHttps?host=&lt;HOST_ADDRESS&gt;&amp;port=&lt;PORT_NUMBER&gt;</li>" +
            "<li>/getSocks4?host=&lt;HOST_ADDRESS&gt;&amp;port=&lt;PORT_NUMBER&gt;</li>" +
            "<li>/getSocks5?host=&lt;HOST_ADDRESS&gt;&amp;port=&lt;PORT_NUMBER&gt;</li>" +
            "</ul>";

    private String generatePACString(String pacType, String host, String port) {
        StringBuilder sb = new StringBuilder("function FindProxyForURL(url, host) {\n");
        if (!TYPE_DIRECT.equalsIgnoreCase(pacType)) {
            sb.append("    if (!isResolvable(host)) {\n");
            sb.append("        return \"").append(pacType).append(' ').append(host).append(':').append(port).append("\";\n");
            sb.append("    }\n");
        }
        sb.append("    return \"DIRECT\";\n");
        sb.append("}\n");
        return sb.toString();
    }

    private ResponseEntity<String> generatePACResponseEntity(String responseString) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-ns-proxy-autoconfig");
        headers.add("Content-Disposition", "inline; filename=\"proxy.pac\"");
        return new ResponseEntity<>(responseString, headers, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<String> getHelp() {
        return new ResponseEntity<>(help, HttpStatus.OK);
    }

    @GetMapping("/getDirect")
    public ResponseEntity<String> getDirectPAC() {
        return generatePACResponseEntity(generatePACString(TYPE_DIRECT, "", ""));
    }

    @GetMapping("/getProxy")
    public ResponseEntity<String> getProxyPAC(@RequestParam(value="host", defaultValue="") String host,
                                      @RequestParam(value="port", defaultValue="") String port) {
        return generatePACResponseEntity(generatePACString(TYPE_PROXY, host, port));
    }

    @GetMapping("/getSocks")
    public ResponseEntity<String> getSocksPAC(@RequestParam(value="host", defaultValue="") String host,
                               @RequestParam(value="port", defaultValue="") String port) {
        return generatePACResponseEntity(generatePACString(TYPE_SOCKS, host, port));
    }

    @GetMapping("/getHttp")
    public ResponseEntity<String> getHttpPAC(@RequestParam(value="host", defaultValue="") String host,
                                      @RequestParam(value="port", defaultValue="") String port) {
        return generatePACResponseEntity(generatePACString(TYPE_HTTP, host, port));
    }

    @GetMapping("/getHttps")
    public ResponseEntity<String> getHttpsPAC(@RequestParam(value="host", defaultValue="") String host,
                               @RequestParam(value="port", defaultValue="") String port) {
        return generatePACResponseEntity(generatePACString(TYPE_HTTPS, host, port));
    }

    @GetMapping("/getSocks4")
    public ResponseEntity<String> getSocks4PAC(@RequestParam(value="host", defaultValue="") String host,
                               @RequestParam(value="port", defaultValue="") String port) {
        return generatePACResponseEntity(generatePACString(TYPE_SOCKS4, host, port));
    }

    @GetMapping("/getSocks5")
    public ResponseEntity<String> getSocks5PAC(@RequestParam(value="host", defaultValue="") String host,
                                      @RequestParam(value="port", defaultValue="") String port) {
        return generatePACResponseEntity(generatePACString(TYPE_SOCKS5, host, port));
    }

}
