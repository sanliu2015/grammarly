@echo off
c:
cd C:\SoftFiles\mongodb-windows-5.0.5\bin
mongo --authenticationDatabase "admin" -quiet "C:\SoftFiles\mongodb-windows-5.0.5\log_rotate.js"
rem 删除3天前的文件
forfiles /p "C:\SoftFiles\mongodb-windows-5.0.5\logs" /s /m *.* /d -3 /c "cmd /c del @path"
