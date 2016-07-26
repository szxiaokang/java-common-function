/**
 * @ClassName:     Captcha.java
 * @author         KangYun 273030282@qq.com
 * @version        V1.0 
 * @Date           2015-12-23 上午11:37:57
 * @Description:   TODO
 *
 */

package com.web.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Captcha {

	/**
	 * 
	 * @param request
	 * @param response
	 * @param key 写入sessionkey
	 * @param len 验证码位数
	 */
	public void captcha1(HttpServletRequest request, HttpServletResponse response, String key, int len) {
		int length = len, fontSize = 20;
		Color backgroundColor = new Color(255, 255, 255);

		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "No-cache");
		response.setDateHeader("Expires", 0);
		// 指定生成的响应是图片
		response.setContentType("image/jpeg");
		int width = 100;
		int height = 26;
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB); // 创建BufferedImage类的对象
		Graphics g = image.getGraphics(); // 创建Graphics类的对象
		Graphics2D g2d = (Graphics2D) g; // 通过Graphics类的对象创建一个Graphics2D类的对象
		Random random = new Random(); // 实例化一个Random对象
		Font mFont = new Font("Arial", Font.BOLD, fontSize); // 通过Font构造字体
		g.setColor(backgroundColor); // 改变图形的当前颜色为随机生成的颜色
		g.fillRect(0, 0, width, height); // 绘制一个填色矩形

		// 画一条折线
		// BasicStroke bs = new BasicStroke(2f, BasicStroke.CAP_BUTT,
		// BasicStroke.JOIN_BEVEL); // 创建一个供画笔选择线条粗细的对象
		// g2d.setStroke(bs); // 改变线条的粗细
		// .setColor(Color.WHITE); // 设置当前颜色为预定义颜色中的深灰色
		// int[] xPoints = new int[3];
		// int[] yPoints = new int[3];
		// for (int j = 0; j < 3; j++) {
		// xPoints[j] = random.nextInt(width - 1);
		// yPoints[j] = random.nextInt(height - 1);
		// }
		// g.drawPolyline(xPoints, yPoints, 3);
		// 生成并输出随机的验证文字
		g.setFont(mFont);
		String sRand = "";
		int itmp = 0;
		for (int i = 0; i < length; i++) {
			if (random.nextInt(2) == 1) {
				itmp = random.nextInt(26) + 97; // 生成A~Z的字母
			} else {
				itmp = random.nextInt(10) + 48; // 生成0~9的数字
			}
			char ctmp = (char) itmp;
			sRand += String.valueOf(ctmp);
			Color color = new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110));
			g.setColor(color);
			/**** 随机缩放文字并将文字旋转指定角度 **/
			// 将文字旋转指定角度
			Graphics2D g2d_word = (Graphics2D) g;
			AffineTransform trans = new AffineTransform();
			trans.rotate(random.nextInt(10) / 10, 25 * i, 20);
			// 缩放文字
			float scaleSize = random.nextFloat() + 0.9f;
			if (scaleSize > 1.1f) {
				scaleSize = 1f;
			}
			trans.scale(scaleSize, scaleSize);
			g2d_word.setTransform(trans);
			/************************/
			g.drawString(String.valueOf(ctmp), i * 20 + 26, 22);

		}
		// 将生成的验证码保存到Session中
		HttpSession session = request.getSession(true);
		session.setAttribute(key, sRand);
		g.dispose();
		try {
			ImageIO.write(image, "JPEG", response.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
