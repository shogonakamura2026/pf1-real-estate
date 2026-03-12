document.addEventListener("DOMContentLoaded", () => {
  const modal = document.getElementById("featureModal");
  const modalTitle = document.getElementById("featureModalTitle");
  const modalImage = document.getElementById("featureModalImage");
  const modalDesc = document.getElementById("featureModalDesc");
  const modalPoints = document.getElementById("featureModalPoints");
  const modalLink = document.getElementById("featureModalLink");

  if (!modal) return;

  const openButtons = document.querySelectorAll(".feature-card--modal");
  const closeButtons = modal.querySelectorAll("[data-modal-close]");

  function openModal(button) {
    const title = button.dataset.modalTitle || "";
    const image = button.dataset.modalImage || "";
    const desc = button.dataset.modalDesc || "";
    const points = (button.dataset.modalPoints || "").split("|").filter(Boolean);
    const link = button.dataset.modalLink || "#";

    modalTitle.textContent = title;
    modalImage.src = image;
    modalImage.alt = title;
    modalDesc.textContent = desc;
    modalLink.href = link;

    modalPoints.innerHTML = "";
    points.forEach((point) => {
      const li = document.createElement("li");
      li.textContent = point;
      modalPoints.appendChild(li);
    });

    modal.classList.add("is-open");
    modal.setAttribute("aria-hidden", "false");
    document.body.classList.add("is-modal-open");
  }

  function closeModal() {
    modal.classList.remove("is-open");
    modal.setAttribute("aria-hidden", "true");
    document.body.classList.remove("is-modal-open");
  }

  openButtons.forEach((button) => {
    button.addEventListener("click", () => openModal(button));
  });

  closeButtons.forEach((button) => {
    button.addEventListener("click", closeModal);
  });

  document.addEventListener("keydown", (e) => {
    if (e.key === "Escape" && modal.classList.contains("is-open")) {
      closeModal();
    }
  });
});