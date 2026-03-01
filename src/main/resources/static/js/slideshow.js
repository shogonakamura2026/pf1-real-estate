/**
 * slideshow.js
 * 役割：トップページのスライドショーを制御する
 *
 * 仕様：
 * - #heroSlider 内の img を順番に表示（is-active を付け替え）
 * - 自動再生（デフォルト 3.5秒）
 * - 画面が非表示のときは停止（負荷軽減）
 * - マウスを乗せている間は一時停止（任意）
 *
 * 前提HTML：
 * <div id="heroSlider" class="hero-slider">
 *   <img class="is-active" ...>
 *   <img ...>
 *   <img ...>
 * </div>
 */

(function () {
  // ===== 設定 =====
  const SLIDER_ID = "heroSlider";
  const INTERVAL_MS = 3500; // スライド切替間隔

  const slider = document.getElementById(SLIDER_ID);
  if (!slider) return;

  const slides = Array.from(slider.querySelectorAll("img"));
  if (slides.length <= 1) return;

  let index = slides.findIndex(img => img.classList.contains("is-active"));
  if (index < 0) index = 0;

  let timer = null;
  let isPaused = false;

  // ===== 表示切替 =====
  function show(nextIndex) {
    slides[index].classList.remove("is-active");
    index = (nextIndex + slides.length) % slides.length;
    slides[index].classList.add("is-active");
  }

  function next() {
    show(index + 1);
  }

  // ===== 自動再生 =====
  function start() {
    if (timer || isPaused) return;
    timer = setInterval(next, INTERVAL_MS);
  }

  function stop() {
    if (!timer) return;
    clearInterval(timer);
    timer = null;
  }

  // ===== タブ非表示時は停止 =====
  document.addEventListener("visibilitychange", function () {
    if (document.hidden) {
      stop();
    } else {
      start();
    }
  });

  // ===== ホバーで一時停止（PC向け） =====
  slider.addEventListener("mouseenter", function () {
    isPaused = true;
    stop();
  });

  slider.addEventListener("mouseleave", function () {
    isPaused = false;
    start();
  });

  // ===== 初期化 =====
  // すべてから is-active を一旦外して、index を正にする（安全策）
  slides.forEach((img, i) => {
    if (i === index) img.classList.add("is-active");
    else img.classList.remove("is-active");
  });

  start();
})();