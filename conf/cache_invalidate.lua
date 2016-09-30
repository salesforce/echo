local invalidate = function(key) 
	local patterns = redis.call('zrange', key, '0', '-1');
	for i,v in pairs(patterns) do
		for _,k in ipairs(redis.call('keys', v)) do 
			redis.call('del', k) 
		end
	end
end
return invalidate(KEYS[1]);
