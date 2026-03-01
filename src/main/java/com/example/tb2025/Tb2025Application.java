package com.example.tb2025;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * アプリケーションのエントリーポイント（起動クラス）。
 *
 * 役割：
 * 
 *   Spring Boot アプリケーションを起動する
 *   ComponentScan により同一パッケージ配下を自動検出する
 *   AutoConfiguration により設定を自動適用する
 * 
 *
 * このクラスが存在するパッケージを基準に、
 * Controller / Repository / Entity が自動スキャンされる。
 */


@SpringBootApplication
public class Tb2025Application {

    /**
     * mainメソッド（Javaの実行開始地点）
     * SpringApplication.run() により組み込みサーバー（Tomcat）が起動する。
     */
    public static void main(String[] args) {
        SpringApplication.run(Tb2025Application.class, args);
    }
}