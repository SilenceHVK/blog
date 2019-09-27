在使用 npm 安装 sass-loader 出现 `gyp verb which failed Error:not found: python2` 错误，通过 stackoverflow 解决方法，设置 npm 环境变量

```bash
   $ npm set SKIP_SASS_BINARY_DOWNLOAD_FOR_CI = true
   $ npm set SKIP_NODE_SASS_TESTS = true
```

之后使用命令清除缓存，再重新安装 `sass-loader`

```bash
   $ npm cache clean --force
   $ npm install sass-loader --dev-save
```
