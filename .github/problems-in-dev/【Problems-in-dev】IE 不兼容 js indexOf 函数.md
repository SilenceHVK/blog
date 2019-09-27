在使用 js 判断数组中是否存在该元素，我们会用到 indexOf 函数。而在 IE 上 indexOf 函数 无法兼容，通过以下方法解决，仅以文章记录一下

```javascript
if (!Array.prototype.indexOf) {
	Array.prototype.indexOf = function(elt /*, from*/) {
		var len = this.length >>> 0;
		var from = Number(arguments[1]) || 0;
		from = from < 0 ? Math.ceil(from) : Math.floor(from);
		if (from < 0) from += len;
		for (; from < len; from++) {
			if (from in this && this[from] === elt) return from;
		}
		return -1;
	};
}
```
