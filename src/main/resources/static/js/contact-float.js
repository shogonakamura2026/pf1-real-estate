/* =========================================================
   お問い合わせフロートパネル制御
   役割：
   ・右下の問い合わせパネルを開閉する
   ・初期表示は「閉じた状態」
   ========================================================= */

/* DOMが完全に読み込まれてから処理を実行する
   （HTML要素が存在しない状態でJSが動くのを防ぐ） */
document.addEventListener("DOMContentLoaded", function () {

  /* 必要なHTML要素を取得 */

  // お問い合わせパネル本体
  const panel = document.getElementById("contactFloat");

  // 「しまう」ボタン
  const closeBtn = document.getElementById("contactFloatClose");

  // 「開く」ボタン（画面端から少し出ているボタン）
  const openBtn = document.getElementById("contactFloatOpen");

  /* 要素が存在しないページでは処理を終了
     （このJSを全ページで読み込んでもエラーにならないようにする） */
  if (!panel || !closeBtn || !openBtn) return;


  /* =========================================================
     初期状態設定
     ========================================================= */

  /* 初期表示は必ず閉じた状態にする
     CSSの .is-hidden クラスでパネルを画面外へ移動 */
  panel.classList.add("is-hidden");

  /* 開くボタンは表示 */
  openBtn.classList.remove("is-hidden");
  
  /* 「⇧」ボタンが被らないように */
  document.body.classList.remove("contact-float-open");


  /* =========================================================
     「しまう」処理
     ========================================================= */

  /* 閉じるボタンがクリックされたとき */
  closeBtn.addEventListener("click", function () {

    // パネルを画面外へ隠す
    panel.classList.add("is-hidden");

    // 開くボタンを表示
    openBtn.classList.remove("is-hidden");
	
	/* 「⇧」ボタンが被らないように */
	document.body.classList.remove("contact-float-open");

  });


  

  /* =========================================================
     「開く」処理
     ========================================================= */

  /* 開くボタンがクリックされたとき */
  openBtn.addEventListener("click", function () {

    // パネルを表示
    panel.classList.remove("is-hidden");

    // 開くボタンを隠す
    openBtn.classList.add("is-hidden");
	
	/* 「⇧」ボタンが被らないように */
	document.body.classList.add("contact-float-open");

  });

});