// Package service 层负责业务类与接口
package service

type IUserService interface {
	GetName(userId int) string
}

type UserService struct {
}

func (u UserService) GetName(userId int) string {
	return "Silence H_VK"
}
