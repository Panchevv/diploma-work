package com.diploma.panchev.nrfcloud.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UtilsTest {

    private final String CERT_VALUE = "-----BEGIN CERTIFICATE-----\nMIIDQTCCAimgAwIBAgITBmyfz5m/jAo54vB4ikPmljZbyjANBgkqhkiG9w0BAQsF\nADA5MQswCQYDVQQGEwJVUzEPMA0GA1UEChMGQW1hem9uMRkwFwYDVQQDExBBbWF6\nb24gUm9vdCBDQSAxMB4XDTE1MDUyNjAwMDAwMFoXDTM4MDExNzAwMDAwMFowOTEL\nMAkGA1UEBhMCVVMxDzANBgNVBAoTBkFtYXpvbjEZMBcGA1UEAxMQQW1hem9uIFJv\nb3QgQ0EgMTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBALJ4gHHKeNXj\nca9HgFB0fW7Y14h29Jlo91ghYPl0hAEvrAIthtOgQ3pOsqTQNroBvo3bSMgHFzZM\n9O6II8c+6zf1tRn4SWiw3te5djgdYZ6k/oI2peVKVuRF4fn9tBb6dNqcmzU5L/qw\nIFAGbHrQgLKm+a/sRxmPUDgH3KKHOVj4utWp+UhnMJbulHheb4mjUcAwhmahRWa6\nVOujw5H5SNz/0egwLX0tdHA114gk957EWW67c4cX8jJGKLhD+rcdqsq08p8kDi1L\n93FcXmn/6pUCyziKrlA4b9v7LWIbxcceVOF34GfID5yHI9Y/QCB/IIDEgEw+OyQm\njgSubJrIqg0CAwEAAaNCMEAwDwYDVR0TAQH/BAUwAwEB/zAOBgNVHQ8BAf8EBAMC\nAYYwHQYDVR0OBBYEFIQYzIU07LwMlJQuCFmcx7IQTgoIMA0GCSqGSIb3DQEBCwUA\nA4IBAQCY8jdaQZChGsV2USggNiMOruYou6r4lK5IpDB/G/wkjUu0yKGX9rbxenDI\nU5PMCCjjmCXPI6T53iHTfIUJrU6adTrCC2qJeHZERxhlbI1Bjjt/msv0tadQ1wUs\nN+gDS63pYaACbvXy8MWy7Vu33PqUXHeeE6V/Uq2V8viTO96LXFvKWlJbYK8U90vv\no/ufQJVtMVT8QtPHRh8jrdkPSHCa2XV4cdFyQzR1bldZwgJcJmApzyMZFo6IQ6XU\n5MsI+yMRQ+hDKXJioaldXgjUkK642M4UwtBV8ob2xJNDd2ZhwLnoQdeXeGADbkpy\nrqXRfboQnoZsG4q5WTP468SQvvG5\n-----END CERTIFICATE-----\n";
    private final String KEY_VALUE = "-----BEGIN RSA PRIVATE KEY-----\\nMIIEpQIBAAKCAQEAy1K9g82GXRmZGqzxScaTAoZWxxXwHY5pp1QMXOSxSeQCN/jn\\nzcgYhWcCwxesuZ/4FrPDmcI7dYZklPUGW8nRKVFFRVztBF3xRvqrheWMiRw7baXt\\njmlhHJeC2rlw7JAUV94e1Ah8PG9ynkZqHFt6YM3j4MxztkQb930MOIEZLcZ1fxe7\\nYPh1PyetCcEqJCkAfA6SJEHINEY/XoZ5HKCykB9iaTqDfjgCtM90+NTYa9+HIOKR\\nAfREqSqp764hlnixyfkOqaIfL81F0roHzUV89febgwkqNmEg1ZoSTZpK/dGL4FSl\\nDhgpqB3zWl/+G/c0uFCvDRr7Vt1DiOr/RJYkQwIDAQABAoIBAQCo37samvzrnNRG\\njLgIH8+AvFWdG8KIuf1VBoESMrxx89hH5wuZJoX/XzSzF5nVv2FOcdaXgfTiw1IN\\nSooSeMip+Z+AYhNWmjRJUPBf0O3t/634f2WU8D/BgLIluvpo8a6D4LBYSgfuJl7b\\nyP5fOTem6btIvmepTl6wK4OX9toT4pLn6JbrvSm/XQQ0zMEnXcxjnJGH3JQwH5S1\\nou33vwzJLocKYuL7qK1ABgBoJPs5rxtOiKEpPNxDVUGaSWsszWEvg9BxvBe7YyMR\\nrREQuhDI+ET58/yY515lg2r1rPTffxUJ6wGZEcwIIVVqqOrR9hvqmGFKm8xFuCA5\\nrzeqR5OpAoGBAPgc+PQZpD+Nh3AVeM1fqrfLCELd9BkTi+FtGrpaR79YDpm3NKvq\\nBVO4zkckfrqj+EpHV/n5pS1XekfCvzW4Qwj9G4BgfISCAQdBOXrGrvynffx4Mhf5\\nRr6NOx9J30FTcc6MbLXswy64rJohSWWUYz9FsmEV8E1OccYbF9BpokwNAoGBANHJ\\nScsLUWGE1xA3Fa2UQl5Wbi4bWaTHIulQ+Vm8I65Z+RH7rWP3aikwdzkY4svbDW0k\\nZSQkjPAyyaOZ/K4k+AHmTxBIxwwwX5QqFPcxYVEijtUJak+kVuRFMUkL8UeoolKR\\nqUhZ3Epksm0tkHQpYZ49p/qt+k4Vfg/+vyfoFQ2PAoGBAL2tuI00hSKo2h/P6nhn\\naKX1q60i7Ze2H7wIouRQXIVlllPhgXzj/YqRv/EXb2LUM97lR9atCZ/uPmdZ9L/m\\nzCi1By9PlCHhGVQogv7foas5iNFYOkOlqHXIlqhG20yftGOJaxpxlI3FVP7H78qe\\ndHlMmSArm3gePlCLKduExjSJAoGANnWPj4MnpC4s+kuieAYV2JGNVrDI66q6WGEp\\n6e6aQKegrdds4peW2ox3Vr4sGgmKibu5+dD3OV+l0q+l1btJGo0kFzEdYv+nwDnQ\\naX9LKRnUhF9GVthtqiKAMdtEznprcQ3ZKSwSsMlFPGxopI8dhUuyMLQ7V0vjBtn1\\n27E2zecCgYEAlp+fk9mt3eKtWxDiiEo9GqAevxvw7adlAa1K3oyX1yazGpay/SFa\\n+C3H+0UXuB3DKJQf7kE4HypqWvxWNkmI2bS9fUzCr6LrfUzQZjMQDZFrIgJNdV2Z\\nJzQPK1Fs5cF/txpuD5/bK2uiaCA36/1DusNRPfXanXH3sYEI+Hwdi6k=\\n-----END RSA PRIVATE KEY-----";

    @Test
    public void testCertificateDecode() {
        byte[] value = Utils.convertPemToByte(CERT_VALUE, "CERTIFICATE");
        assertEquals(837, value.length);
    }

    @Test
    public void testKeyDecode() {
        byte[] value = Utils.convertPemToByte(KEY_VALUE, "RSA PRIVATE KEY");
        assertEquals(1212, value.length);
    }
}
