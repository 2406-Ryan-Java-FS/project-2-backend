#Displays time, branch, author, commit message, and files changed recently
git fetch
git log --all --name-status --pretty=format:'%C(yellow)%h %C(red)%ad %C(cyan)%d %C(green)%an %C(cyan)%s' --date=format-local:%Y-%m-%d_%H:%M
