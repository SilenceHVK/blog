package leetcode

import "testing"

// https://leetcode-cn.com/problems/valid-parentheses/submissions/
func isValid(s string) bool {
	if len(s)%2 == 1 {
		return false
	}

	stack := []string{}
	for _, c := range s {
		paren := string(c)
		if paren == "{" || paren == "[" || paren == "(" {
			stack = append(stack, paren)
		} else {
			stackLength := len(stack)
			if stackLength == 0 {
				return false
			}
			topChar := stack[stackLength-1]
			if (paren == "}" && topChar != "{") || (paren == ")" && topChar != "(") || (paren == "]" && topChar != "[") {
				return false
			}
			stack = stack[:stackLength-1]
		}
	}
	return len(stack) == 0
}

func TestIsValid(t *testing.T) {
	t.Log(isValid("[)"))
}
