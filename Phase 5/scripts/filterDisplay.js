const icon = document.querySelector("#filter_icon");
const wrapper = document.querySelector("#filter_wrapper");

wrapper.style.display = window.getComputedStyle(wrapper, null).display;

icon.addEventListener("click", () => {
  mode = wrapper.style.display;
  if (mode == 'none')
    wrapper.style.display = 'block';
  else
    wrapper.style.display = 'none';
});