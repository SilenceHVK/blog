package utils

import (
	"bufio"
	"bytes"
	"os"
	"os/exec"
	"testing"
)

// exec 可以执行程序外部命令

// 输出执行结果 stdout 和 stderr 一起输出
func TestPrintExecResult(t *testing.T) {
	command := exec.Command("ls", "-lah")
	output, err := command.CombinedOutput()
	if err != nil {
		panic(err)
	}
	t.Logf("%s", output)
}

// 将 stdout 和 stderr 分开输出
func TestPrintStdoutAndStderr(t *testing.T) {
	command := exec.Command("ls", "-lah")

	var stdout, stderr bytes.Buffer
	command.Stdout = &stdout
	command.Stderr = &stderr

	err := command.Run()
	if err != nil {
		panic(err)
	}

	t.Logf("stdout: %s\n stderr: %s", stdout.Bytes(), stderr.Bytes())
}

// 在执行过程中输出执行结果
func TestPrintExecution1(t *testing.T) {
	command := exec.Command("ls", "-lah")

	stdoutPipe, _ := command.StdoutPipe()
	stderrPipe, _ := command.StderrPipe()

	_ = command.Start()

	go func() {
		scanner := bufio.NewScanner(stdoutPipe)
		for scanner.Scan() {
			t.Log("stdout: ", scanner.Text())
		}
	}()

	go func() {
		scanner := bufio.NewScanner(stderrPipe)
		for scanner.Scan() {
			t.Log("stderr: ", scanner.Text())
		}
	}()

	err := command.Wait()
	if err != nil {
		panic(err)
	}
}

// 更改程序环境变量
func TestAlterEnv(t *testing.T) {
	command := exec.Command("echo", "$TEST")
	command.Env = append(os.Environ(), "TEST=hello world")
	output, err := command.CombinedOutput()
	if err != nil {
		panic(err)
	}
	t.Logf("%s", output)
}

// 检查程序是否存在
func TestCheckExists(t *testing.T) {
	path, err := exec.LookPath("java")
	if err != nil {
		panic(err)
	}
	t.Log(path)
}

// 使用管道实现两个命令的关联
func TestExecPipe(t *testing.T) {
	c1 := exec.Command("ls")
	c2 := exec.Command("wc", "-l")
	c2.Stdin, _ = c1.StdoutPipe()
	c2.Stdout = os.Stdout
	_ = c2.Start()
	_ = c1.Run()
	_ = c2.Wait()
}
