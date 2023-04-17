package k8s_client

import (
	"context"
	_ "embed"
	metav1 "k8s.io/apimachinery/pkg/apis/meta/v1"
	"k8s.io/apimachinery/pkg/apis/meta/v1/unstructured"
	"k8s.io/apimachinery/pkg/runtime/schema"
	"k8s.io/apimachinery/pkg/util/yaml"
	"testing"
)

var k8sClient *K8SClient

func init() {
	k8sClient = NewK8SClient("https://master.cluster.local:6443", "/Users/h_vk/Documents/VM/.kube/config")
}

func TestNodes(t *testing.T) {
	nodes, _ := k8sClient.Nodes()
	for _, node := range nodes.Items {
		t.Log(node.Name, node.Status)
	}
}

//go:embed resource/nginx.yaml
var resourceYaml string

func TestDynamicClient(t *testing.T) {

	deployObj := &unstructured.Unstructured{}
	if err := yaml.Unmarshal([]byte(resourceYaml), deployObj); err != nil {
		panic(err)
	}

	// 定义 k8s 资源
	groupVersionResource := schema.GroupVersionResource{
		Group:    "",
		Version:  "v1",
		Resource: "pods",
	}

	_, err := k8sClient.DynamicClient.Resource(groupVersionResource).Namespace("default").Create(context.TODO(), deployObj, metav1.CreateOptions{})
	if err != nil {
		panic(err)
	}
}

func TestDiscoveryClient(t *testing.T) {
	resources, _ := k8sClient.DiscoveryClient.ServerPreferredResources()
	for _, resource := range resources {
		t.Log(resource)
	}
}
