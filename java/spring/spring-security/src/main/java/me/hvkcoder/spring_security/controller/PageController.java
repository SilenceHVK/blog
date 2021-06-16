package me.hvkcoder.spring_security.controller;

import com.google.code.kaptcha.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author h_vk
 * @since 2021/6/15
 */
@Controller
public class PageController {

  @Autowired private Producer captchaProducer;

  @GetMapping({"/login", "/login.html", "/login.htm"})
  public String login() {
    return "login";
  }

  /**
   * 获取验证码
   *
   * @param request
   * @param response
   */
  @GetMapping("/captcha.png")
  public void getCaptcha(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    // 设置响应类型
    response.setContentType("image/png");
    // 创建验证码文本
    final String text = captchaProducer.createText();
    // 将验证码设置到 session 中
    request.getSession().setAttribute("captcha", text);
    // 生成验证码图片
    BufferedImage captchaImg = captchaProducer.createImage(text);
    // 写入验证码图片
    try (ServletOutputStream outputStream = response.getOutputStream()) {
      ImageIO.write(captchaImg, "png", outputStream);
      outputStream.flush();
    }
  }
}
