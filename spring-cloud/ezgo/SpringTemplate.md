# SpringTemplate

## RestTemplate

```java
// 返回对象为响应体中数据转化成的对象，基本可以理解为json
// Post请求
org.springframework.web.client.RestTemplate#postForObject(java.lang.String, java.lang.Object, java.lang.Class<T>, java.lang.Object...)

// Get请求
org.springframework.web.client.RestTemplate#getForObject(java.lang.String, java.lang.Class<T>, java.lang.Object...)
    
// 返回对象为ResponseEntity对象，包含响应中一些重要信息，比如响应头、响应状态码、响应体等
// Post请求
org.springframework.web.client.RestTemplate#postForEntity(java.lang.String, java.lang.Object, java.lang.Class<T>, java.lang.Object...)

// Get请求
org.springframework.web.client.RestTemplate#getForEntity(java.lang.String, java.lang.Class<T>, java.lang.Object...)
```

