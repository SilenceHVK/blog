package k8s_client

import (
	"context"
	v1 "k8s.io/api/core/v1"
	metav1 "k8s.io/apimachinery/pkg/apis/meta/v1"
	"k8s.io/client-go/discovery"
	"k8s.io/client-go/dynamic"
	"k8s.io/client-go/kubernetes"
	"k8s.io/client-go/rest"
	"k8s.io/client-go/tools/clientcmd"
	"log"
)

type K8SClient struct {
	Config          *rest.Config
	Clientset       *kubernetes.Clientset
	DynamicClient   *dynamic.DynamicClient
	DiscoveryClient *discovery.DiscoveryClient
	Namespace       *Namespace
}

// LoadConfig 加载 Kubernetes 配置文件
func (client *K8SClient) LoadConfig(masterUrl string, kubeConfigPath string) *rest.Config {
	config, err := clientcmd.BuildConfigFromFlags(masterUrl, kubeConfigPath)
	if err != nil {
		log.Fatal(err)
	}
	client.Config = config
	return config
}

// InitClientset 创建 Clientset 实例常用于对 K8S 内部资源做 CRUD 或 查询当前集群拥有什么样的资源
func (client *K8SClient) InitClientset(config *rest.Config) *kubernetes.Clientset {
	clientSet, err := kubernetes.NewForConfig(config)
	if err != nil {
		log.Fatal(err)
	}
	client.Clientset = clientSet
	return clientSet
}

// InitDynamicClient 创建 DynamicClient 实例常用于自定义资源
func (client *K8SClient) InitDynamicClient(config *rest.Config) *dynamic.DynamicClient {
	dynamicClient, err := dynamic.NewForConfig(config)
	if err != nil {
		log.Fatal(err)
	}
	client.DynamicClient = dynamicClient
	return dynamicClient
}

// InitDiscoveryClient 创建 DiscoveryClient 实例用于获取当前集群有什么资源 kubectl api-resources 就是用它实现的
func (client *K8SClient) InitDiscoveryClient(config *rest.Config) *discovery.DiscoveryClient {
	discoveryClient, err := discovery.NewDiscoveryClientForConfig(config)
	if err != nil {
		log.Fatal(err)
	}
	client.DiscoveryClient = discoveryClient
	return discoveryClient
}

// Nodes 获取 K8S 节点信息
func (client *K8SClient) Nodes() (*v1.NodeList, error) {
	return client.Clientset.CoreV1().Nodes().List(context.TODO(), metav1.ListOptions{})
}

// NewK8SClient 初始化完整的 k8sClient 实例
func NewK8SClient(masterUrl string, kubeConfigPath string) *K8SClient {
	client := &K8SClient{}
	client.LoadConfig(masterUrl, kubeConfigPath)
	client.InitClientset(client.Config)
	client.InitDynamicClient(client.Config)
	client.InitDiscoveryClient(client.Config)
	return client
}

// Default 按默认配置初始化 K8S 配置
func Default() *K8SClient {
	return &K8SClient{}
}
