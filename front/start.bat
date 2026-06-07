@echo off
start "" cmd /c "cd /d %~dp0\marketback & npm run dev"
start "" cmd /c "cd /d %~dp0\marketproject & npm run dev"
exit