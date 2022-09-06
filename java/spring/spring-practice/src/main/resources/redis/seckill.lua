local inventoryKey = KEYS[1]
if tonumber(redis.call("get", inventoryKey)) <= 0 then
	return -1
else
	redis.call("decr", inventoryKey)
end
return 1