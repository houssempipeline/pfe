import os
import requests
import json

# CONFIGURATION
GITHUB_REPO = os.environ.get("GITHUB_REPO")
if not GITHUB_REPO:
    raise EnvironmentError("GITHUB_REPO environment variable is not set.")

# Extract token and repo path from GITHUB_REPO
try:
    token = GITHUB_REPO.replace("https://", "").split("@")[0]
except Exception:
    raise ValueError("Failed to extract token from GITHUB_REPO URL.")

repo_path = GITHUB_REPO.split("@")[-1].replace("github.com/", "").replace(".git", "")

# GitHub API headers
headers = {
    'Authorization': f'token {token}',
    'Accept': 'application/vnd.github+json'
}

# Load SonarQube report
REPORT_PATH = 'sonar-report.json'
with open(REPORT_PATH, 'r') as f:
    data = json.load(f)
issues = data['issues']

# Fetch existing GitHub issue titles to prevent duplicates
existing_titles = set()
page = 1
while True:
    r = requests.get(
        f'https://api.github.com/repos/{repo_path}/issues?state=open&page={page}',
        headers=headers
    )
    if r.status_code != 200:
        raise RuntimeError(f"GitHub API error while listing issues: {r.json()}")
    existing = r.json()
    if not existing:
        break
    for issue in existing:
        existing_titles.add(issue['title'])
    page += 1

# Process and create issues
for issue in issues:
    title = f"[{issue['severity']}] {issue['message']}"
    if title in existing_titles:
        print(f"Skipped (already exists): {title}")
        continue

    component = issue['component'].split(':')[-1]
    line = issue.get('line', 1)
    url = f"https://github.com/{repo_path}/blob/main/{component}#L{line}"
    description = (
        f"**Rule:** {issue['rule']}\n"
        f"**File:** `{component}`\n"
        f"**Line:** {line}\n"
        f"**Message:** {issue['message']}\n"
        f"[View Code]({url})"
    )

    payload = {'title': title, 'body': description}
    response = requests.post(
        f'https://api.github.com/repos/{repo_path}/issues',
        headers=headers,
        json=payload
    )

    if response.status_code == 201:
        print(f"Issue created: {title}")
    else:
        print(f"Failed to create issue: {title}")
        print(response.json())
