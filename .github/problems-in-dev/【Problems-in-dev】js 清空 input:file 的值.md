由于 javascript 不能清除 `input:file` 上传控件的值，因此最好的方法是在 `input:file` 上传控件的外层嵌入 `<form>` 元素，使用 `<form>` 元素的 `reset()` 方法来清除`input:file` 上传控件的值。代码如下：

```javascript
function clearFileInput(file) {
	var form = document.createElement('form');
	document.body.appendChild(form);
	var pos = file.nextSibling;
	form.appendChild(file);
	form.reset();
	pos.parentNode.insertBefore(file, pos);
	document.body.removeChild(form);
}
```
