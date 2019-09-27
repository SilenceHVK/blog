## 基础设置

- 关闭 Rootless

```
1.系统启动进入安全模式（cmd + R）
2.找到实用工具 -> ternimal
输入：csrutil disable （关闭） 或 csrutil enable（启动）
3.重启系统
```

- 设置 root 用户

```bash
## 创建 root 用户
sudo passwd root

## 登录 root 用户
su root
```

- 显示隐藏文件

```bash
defaults write com.apple.finder AppleAllFiles -bool true
## OR
defaults write com.apple.finder AppleShowAllFiles YES

## 重启 finder
```

- 解决文件已损坏，打不开的问题

```bash
sudo spctl --master-disable
```

- 设置 `CommandLineTools`

```bash
## 安装 XCode

## 设置 CommandLineTools 路径
sudo xcode-select -switch /Applications/Xcode.app/Contents/Developer
```

## Homebrew

```bash
## 安装 Homebrew
/usr/bin/ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"

## 安装：brew install 软件名称
## 卸载：brew uninstall 软件名称
## 已安装的软件目录： brew list
## 更新 brew ：brew update
## 清理：brew cleanup
## 检查更新版本：brew outdated
## 升级：brew upgrade
```

## Homebrew Cask

```bash
## 安装 Homebrew
brew tap caskroom/cask  // 添加 Github 上的 caskroom/cask 库
brew install brew-cask  // 安装 brew-cask

## 搜索：brew cask search 软件名称
## 安装：brew cask install 软件名称

## 推荐安装软件
brew cask install alfred
brew cask install google-chrome
```

## iTerm2 + zsh

```bash
## 安装iTerm2
brew cask install iTerm2

## 安装 zsh
brew install zsh zsh-completions

## 安装oh-my-zsh
curl -L https://raw.github.com/robbyrussell/oh-my-zsh/master/tools/install.sh | ZSH=~/.oh-my-zsh/ sh

## 创建zsh的配置文件
cp ~/.oh-my-zsh/templates/zshrc.zsh-template ~/.zshrc

## 设置 zsh 为默认的shell
chsh -s /bin/zsh

## 推荐插件
cd ~/.oh-my-zsh/custom/plugins
## 语法高亮
git clone git://github.com/zsh-users/zsh-syntax-highlighting.git
## 代码自动补全
git clone git://github.com/zsh-users/zsh-autosuggestions

```

## Mac Node.js 环境搭建

```bash
## nvm 安装 用于管理 node 版本
## 删除全局 node_modules 目录
sudo rm -rf /usr/local/lib/node_modules

## 删除 node
sudo rm /usr/local/bin/node

## 删除全局 node 模块注册的软链
cd  /usr/local/bin && ls -l | grep "../lib/node_modules/" | awk '{print $9}'| xargs rm

## 安装 nvm
git clone https://github.com/creationix/nvm.git ~/.nvm && cd ~/.nvm && git checkout `git describe --abbrev=0 --tags`


## 安装指定版本，可模糊安装
nvm install <version>
## 删除已安装的指定版本
nvm uninstall <version>
## 设置全局默认版本
nvm alias default <version>
## 当前命令行使用 node 版本
nvm use <version>
## 列出所有安装的版本
nvm ls
## 列出所以远程服务器的版本
nvm ls-remote
## 显示当前的版本
nvm current
## 删除已定义的别名
nvm unalias
## 在当前版本node环境下，重新全局安装指定版本号的npm包
nvm reinstall-packages <version>


## cnpm 安装
npm install -g cnpm --registry=https://registry.npm.taobao.org

## node 需要安装 npm 包
npm install -g node-gyp
npm install -g node-pre-gyp

```

## SDKMan 管理

```



## 安装 sdkman

## 设置默认路径
export SDKMAN_DIR="/usr/local/sdkman" && curl -s "https://get.sdkman.io" | bash

source "$HOME/.sdkman/bin/sdkman-init.sh"

## 查看支持的软件
sdk list

## 列出软件版本
sdk list 软件名称

## 安装本地包
sdk install 软件名称 版本号 本地地址

## 选择终端使用版本
sdk use 软件名称 版本号

## 设置默认版本
sdk default 软件名称 版本号

## 查看当前使用的版本
sdk current 软件名称
```

## Mac Java 环境搭建

```bash
## maven 安装
brew install maven

## idea 破解
1. 打开IDEA的JVM配置文件，一般会在C:\Users\用户名\.IntelliJIdea2018.1\config下的idea64.exe.vmoptions文件（32位idea.exe.vmoptions）,如果找不到可以在IDEA中点击”Configure” -> “Edit Custom VM Options …”自动打开或者自己新建一个

2. 在该文件最后添加一句:-javaagent:{你刚刚下载的补丁的路径} 例如:-javaagent:C:\Users\34862\Downloads\JetbrainsCrack.jar

3. 重启IDEA

4. 在激活对话框中选Activation code 随便输入 点击OK

{
    "licenseId":"ThisCrackLicenseId",//随便填
    "licenseeName":"hvkcoder",//你的名字
    "assigneeName":"hvkcoder",//你的名字
    "assigneeEmail":"hvkcoder@163.com",//你的邮箱
    "licenseRestriction":"Thanks Rover12421 Crack",//激活信息
    "checkConcurrentUse":false,
    "products":[//各个产品的代码以及过期时间
        {"code":"II","paidUpTo":"2099-12-31"},
        {"code":"DM","paidUpTo":"2099-12-31"},
        {"code":"AC","paidUpTo":"2099-12-31"},
        {"code":"RS0","paidUpTo":"2099-12-31"},
        {"code":"WS","paidUpTo":"2099-12-31"},
        {"code":"DPN","paidUpTo":"2099-12-31"},
        {"code":"RC","paidUpTo":"2099-12-31"},
        {"code":"PS","paidUpTo":"2099-12-31"},
        {"code":"DC","paidUpTo":"2099-12-31"},
        {"code":"RM","paidUpTo":"2099-12-31"},
        {"code":"CL","paidUpTo":"2099-12-31"},
        {"code":"PC","paidUpTo":"2099-12-31"},
        {"code":"DB","paidUpTo":"2099-12-31"},
        {"code":"GO","paidUpTo":"2099-12-31"},
        {"code":"RD","paidUpTo":"2099-12-31"}
    ],
    "hash":"2911276/0",
    "gracePeriodDays":7,
    "autoProlongated":false
}

## idea 配置 spring boot 热更新
1. setting 勾选 Compiler > Build project automatically
2. cmd + shift+ a 勾选 Registry... > compiler.automake.allow.when.app.running
```

## Docker 安装与 Kubernetes 单节点部署 （可选）

```
## 安装 Docker
brew cask install docker

## Kubernetes 需要的镜像
k8s.gcr.io/kube-proxy-amd64:v1.10.3=registry.cn-hangzhou.aliyuncs.com/google_containers/kube-proxy-amd64:v1.10.3
k8s.gcr.io/kube-controller-manager-amd64:v1.10.3=registry.cn-hangzhou.aliyuncs.com/google_containers/kube-controller-manager-amd64:v1.10.3
k8s.gcr.io/kube-scheduler-amd64:v1.10.3=registry.cn-hangzhou.aliyuncs.com/google_containers/kube-scheduler-amd64:v1.10.3
k8s.gcr.io/kube-apiserver-amd64:v1.10.3=registry.cn-hangzhou.aliyuncs.com/google_containers/kube-apiserver-amd64:v1.10.3
k8s.gcr.io/k8s-dns-dnsmasq-nanny-amd64:1.14.8=registry.cn-hangzhou.aliyuncs.com/google_containers/k8s-dns-dnsmasq-nanny-amd64:1.14.8
k8s.gcr.io/k8s-dns-sidecar-amd64:1.14.8=registry.cn-hangzhou.aliyuncs.com/google_containers/k8s-dns-sidecar-amd64:1.14.8
k8s.gcr.io/k8s-dns-kube-dns-amd64:1.14.8=registry.cn-hangzhou.aliyuncs.com/google_containers/k8s-dns-kube-dns-amd64:1.14.8
k8s.gcr.io/pause-amd64:3.1=registry.cn-hangzhou.aliyuncs.com/google_containers/pause-amd64:3.1
k8s.gcr.io/kubernetes-dashboard-amd64:v1.10.0=registry.cn-hangzhou.aliyuncs.com/google_containers/kubernetes-dashboard-amd64:v1.10.0
k8s.gcr.io/etcd-amd64:3.1.12=registry.cn-hangzhou.aliyuncs.com/google_containers/etcd-amd64:3.1.12
```

批量下载镜像脚本

```bash

#/bin/bash

file="images"

if [ -f "$file" ]
then
  echo "$file found."

  while IFS='=' read -r key value
  do
    #echo "${key}=${value}"
    docker pull ${value}
    docker tag ${value} ${key}
    docker rmi ${value}
  done < "$file"

else
  echo "$file not found."
fi
```

安装 dashboard

```bash
kubectl create -f https://raw.githubusercontent.com/kubernetes/dashboard/master/aio/deploy/recommended/kubernetes-dashboard.yaml

## 访问地址
http://localhost:8001/api/v1/namespaces/kube-system/services/https:kubernetes-dashboard:/proxy/
```

安装 helm

```bash
brew install kubernetes-helm

helm init --upgrade --tiller-image registry.cn-hangzhou.aliyuncs.com/google_containers/tiller:v2.12.0 --stable-repo-url https://kubernetes.oss-cn-hangzhou.aliyuncs.com/charts

kubectl create serviceaccount --namespace kube-system tiller

kubectl create clusterrolebinding tiller-cluster-rule --clusterrole=cluster-admin --serviceaccount=kube-system:tiller

kubectl patch deploy --namespace kube-system tiller-deploy -p '{"spec":{"template":{"spec":{"serviceAccount":"tiller"}}}}'
```

## Mac 查看 vmnet8 的网关地址

```bash
## 查找 vmnet8 存放的位置
find / -name vmnet8

## 进入 目录，查看 nat.conf 文件
cd /Library/Preferences/VMware\ Fusion/vmnet8
```

## Mac 常用命令

```
## 查看指定端口号进程
lsof -i tcp:端口号

## 杀死进程
kill pid

## 查看软件安装目录
which softwareName
```
