# 🐙 Lab 6: Version control

This group project statemnt is part of the course **Software Engineering** of Universidad San Jorge of Zaragoza. All contents are original.

> ⚠️ **NOTE**: This lab is considered theoretical content. Concept pills and commands learned during this lab may be asked in January exam.

Welcome to your hands-on Git practice! This is a **team lab** designed for 3-4 people working together, not necessarely the same as in your group projects. You'll learn real-world Git workflows by managing a university cafeteria menu system.

## 📋 Team members

| Name | GitHub Username |
|------|-----------------|
| David Torrubia Santos | TearingMoon |
| Jorge Kojtyth Trevijano | Koyfc |
| Carlos Puig Cuadra | CarlosLaPulga |


## 📝 Lab rules

### 🎯 Workflow rules

1. **Never push directly to main** (all changes through pull requests)
2. **Every PR needs one approval** (your teammate reviews your work)
3. **CI must be green before merging** (all automated checks must pass)
4. **Keep commits small and focused** (one logical change per commit)
5. **Write clear commit messages** (follow conventions, see Task 1)

Lab will be graded based on this rules.

### ⚡ Using Git commands

**IMPORTANT:** This lab must be completed using Git commands from the terminal/command line:
- ❌ **DO NOT use** IDEs with graphical Git interfaces (VSCode Git, IntelliJ Git, etc.)
- ❌ **DO NOT use** visual applications (SourceTree, GitKraken, GitHub Desktop, etc.)
- ✅ **DO use** Git commands in the terminal

**Why commands?** While visual tools are commonly used in the real world, understanding basic Git commands is fundamental. Moreover, in many situations (servers, scripts, CI/CD), you'll only have access to the command line, and it's often even faster.

## 🎯 Learning goals

By the end of this practice, you will:
- ✅ Make clean commits with meaningful messages using proper conventions
- ✅ Work with branches and pull requests like a professional team
- ✅ Review code and provide helpful feedback to teammates
- ✅ Resolve merge conflicts without panic
- ✅ Understand how `.gitignore` protects sensitive data
- ✅ Use git stash to manage work in progress
- ✅ Create and manage version tags

---

## 🛠️ Pre-lab: Repository setup

### Setting up your team repository

1. **Fork the repository** (only one team member does this):
   - Navigate to the [original repository URL](https://github.com/usj-software-engineering/git-lab)
   - Click the **Fork** button (top right corner)
   - Select your GitHub account as the destination
   - Wait for the fork to complete
   - Once the fork is completed, form your forked repo go to **Actions** →  **I understand, enable workflows**

2. **Add your teammates as collaborators**:
   - In YOUR forked repository, go to **Settings** → **Collaborators and teams** →  **Manage access**
   - Click **Add people**
   - Add your teammates by their GitHub usernames
   - They'll receive an email invitation to accept

3. **Everyone clones the repository**:
   ```bash
   # Each team member runs this with the fork URL
   git clone https://github.com/[your-username]/git-lab.git
   cd git-lab

   # Verify you're in the right place
   ls -la
   ```

4. **Configure Git** (everyone does this):
   ```bash
   git config --global user.name "Your name"
   git config --global user.email "your.email@alu.usj.es"

   # Verify configuration
   git config --list
   ```

5. **Add team names to `README.md`**
   This is the only change that will commit directly to `main`
   ```bash
   # Open README.md in your editor
   # Edit the Team members sectiom

   # Stage (we will focus on this later)
   git add README.md

   # Commit with a descriptive message (we will focus on this later)
   git commit -m "update: team members names"
   
   # Upload changes (we will focus on this later)
   git push origin main

6. **Configure branch protection** (important!):
   - Go to **Settings** → **Branches**
   - Click **Add branch ruleset**
   - Rulset name: "Main branch protection"
   - Target branches → Add target → Include by pattern → Branch naming pattern: `main`
   - Check these boxes:
     - ✅ Require a pull request before merging
     - ✅ Require approvals (set to 1)
     - ✅ Dismiss stale pull request approvals when new commits are pushed
     - ✅ Block force pushes
     - ✅ Require status checks to pass
      - ✅ Press **Add checks** and look for **Check Menu Quality**. If this check does not appear, make sure that, under **Actions**, the **Check Menu Quality** has been executed at least once.
   - Click **Create** at the bottom


---

## 📚 Tasks

<details>
<summary><strong>🔒 Step 0: Verify the protections</strong></summary>

> **🧪 Concept pill: Protected branches**
>
> **What it is:** Branch protection prevents direct changes to important branches
>
> **Real-world analogy:** Like a safety lock on dangerous machinery (requires multiple steps to activate)
>
> **Why it matters:** Prevents accidental damage to production code. Forces peer review.
>
> **Pro tip:** If you can push directly to main, your repository isn't properly protected!

### Your task:
1. **Try to push directly to main** (this MUST fail):
   ```bash
   echo "test" >> README.md
   git add README.md
   git commit -m "test: verify branch protection"
   git push origin main
   ```

2. **Save the error message as proof**:
   ```bash
   # Create a feature branch for your proof
   git checkout -b feature/protection-proof

   # Create proof directory and save error
   mkdir -p proof
   echo "[Paste the actual error message here]" > proof/push_blocked.txt

   # Commit and push
   git add proof/push_blocked.txt
   git commit -m "add: proof of branch protection"
   git push -u origin feature/protection-proof
   ```

3. **Clean up your local main branch**:
   ```bash
   git checkout main
   git reset --hard origin/main
   ```

</details>

---

<details>
<summary><strong>🎯 Task 1: Local flow (your first commits)</strong></summary>

> **🧪 Concept pill: The three stages of Git**
>
> **What they are:**
> - **Working directory:** Where you edit files (your desk)
> - **Staging area:** Files ready to commit (your outbox)
> - **Repository:** Permanent history (filed away)
>
> **Commands to move between stages:**
> - `git add` → moves from working to staging
> - `git commit` → moves from staging to repository
> - `git status` → shows you where everything is
>
> **Pro tip:** Use `git diff` before staging to review your changes!

### 🔄 Commit message conventions

Good commit messages are crucial for project maintainability. Use these prefixes to categorize your commits:

| Prefix | Purpose | Example |
|--------|---------|---------|
| `feat:` | New feature | `feat: add user authentication` |
| `add:` | Add content/files | `add: new menu items to cafeteria` |
| `remove:` | Remove content/files | `remove: deprecated API endpoints` |
| `fix:` | Bug fix | `fix: resolve null pointer in payment` |
| `chore:` | Maintenance tasks | `chore: update dependencies` |
| `docs:` | Documentation changes | `docs: update README installation steps` |
| `refactor:` | Refactoring without changing functionality | `refactor: extract validation logic` |
| `test:` | Add or modify tests | `test: add unit tests for menu service` |
| `style:` | Format/style changes | `style: fix indentation in Java files` |

#### ✅ Good vs ❌ bad commit messages

| ❌ Bad | ✅ Good | Why it's better |
|--------|---------|-----------------|
| "fixed stuff" | "fix: resolve payment validation error" | Specific about what was fixed |
| "WIP" | "feat: add menu search (work in progress)" | Describes what's being worked on |
| "asdfasdf" | "chore: update git configuration" | Meaningful and searchable |
| "." | "add: daily specials to menu" | Explains the actual change |

**Tips for good commit messages:**
- Start with a verb in imperative mood (add, fix, update, not added, fixed, updated)
- Keep the first line under 50 characters
- Be specific but concise
- Reference issue numbers when applicable: `fix: resolve login bug (#123)`

### Your task:

1. **Check your starting point**:
   ```bash
   git status
   git branch  # Should show * main
   ```

2. **Make your first change** (add a food item):
   ```bash
   # Open src/Food.java in your editor
   # Add a new line like: System.out.println("Pizza - $8.99");

   # See what changed
   git diff

   # Stage interactively (review each change)
   git add -p src/Food.java

   # Commit with a descriptive message
   git commit -m "<write your own commit message>"
   ```

3. **Make your second change** (add a drink):
   ```bash
   # Edit src/Drinks.java
   # Add: System.out.println("Lemonade - $3.50");

   git diff
   git add src/Drinks.java
   git commit -m "<write your own commit message>"
   ```

4. **Review your work**:
   ```bash
   git log --oneline -5
   ```

</details>

---

<details>
<summary><strong>🎯 Task 1.5: Protecting secrets with .gitignore</strong></summary>

> **🧪 Concept pill: The .gitignore file**
>
> **What it is:** A special file that tells Git what to ignore
>
> **Real-world analogy:** Like a "Do Not Scan" list at airport security (some items shouldn't go through)
>
> **Common ignores:**
> - Passwords and API keys (`.env`, `credentials.txt`)
> - System files (`.DS_Store`, `Thumbs.db`)
> - Build artifacts (`*.class`, `target/`)
> - Dependencies (`node_modules/`)
>
> **Critical rule:** Once committed, files are in history forever (add to .gitignore BEFORE committing!)

### Your task:

1. **Create a fake credentials file**:
   ```bash
   # Create a file with fake sensitive data
   echo "database_password=cafeteria123" > src/credentials.txt
   echo "api_key=SECRET_KEY_DO_NOT_SHARE" >> src/credentials.txt
   cat src/credentials.txt  # Verify it was created
   ```

2. **See the danger** (Git wants to track it):
   ```bash
   git status
   # Notice: src/credentials.txt appears as untracked!
   ```

3. **Accidentally stage it** (we'll fix this!):
   ```bash
   git add .
   git status
   # 😱 Oh no! credentials.txt is staged (green)!
   ```

4. **EMERGENCY FIX** (unstage immediately):
   ```bash
   git reset src/credentials.txt
   git status
   # It's back to untracked (red) 😅
   ```

5. **Properly ignore the file**:
   ```bash
   # Add to .gitignore
   echo "src/credentials.txt" >> .gitignore
   echo "*.log" >> .gitignore  # Also ignore log files

   # Verify it's now ignored
   git status
   # credentials.txt should NOT appear anymore!
   ```

6. **Commit the .gitignore update**:
   ```bash
   git add .gitignore
   git commit -m "chore: update .gitignore to protect credentials"
   ```

7. **Test that it works** (create a log file):
   ```bash
   echo "Debug information" > debug.log
   git status
   # debug.log should NOT appear (it's ignored)!
   ```

</details>

---

<details>
<summary><strong>🎯 Task 2: Branch and push (going remote)</strong></summary>

> **🧪 Concept pill: Branches & remotes**
>
> **Branches:** Parallel universes for your code
> - `main` = production, stable, protected
> - Feature branches = experiments, new work
> - Name pattern: `feature/description-of-work`
>
> **Remote (origin):** Your team's shared GitHub repository
> - Local = on your computer
> - Remote = on GitHub
> - `push` = upload, `pull` = download
>
> **Golden rule:** Never work directly on main, always use branches!

> **🧪 Concept pill: Checkout vs branch vs switch**
>
> **Historical evolution:**
> - `git checkout` (the original Swiss Army knife command, does too many things)
> - `git switch` (new command, Git 2.23+, specifically for switching branches)
> - `git branch` (creates branches but doesn't switch to them)
>
> **Modern best practices:**
> ```bash
> # Old way (still works)
> git checkout -b new-branch      # Create and switch
> git checkout main               # Switch to existing
>
> # New way (clearer intent)
> git switch -c new-branch        # Create and switch
> git switch main                 # Switch to existing
> git branch new-branch           # Just create, don't switch
> ```
>
> **Why the change?** `checkout` was overloaded (it switches branches AND restores files). The new commands are clearer about what they do.

### Your task:

1. **Create your feature branch**:
   ```bash
   # Using traditional checkout (what you'll see in older codebases)
   git checkout -b feature/menu-improvements

   # Or using modern switch command
   # git switch -c feature/menu-improvements
   ```

2. **Add a special item that will trigger CI failure**:
   ```bash
   # Edit src/DailySpecials.java
   # Add: System.out.println("Tuesday: SOLD OUT - Tacos - $8.99");

   git add src/DailySpecials.java
   git commit -m "add: Tuesday taco special (currently sold out)"
   ```

3. **Push to GitHub**:
   ```bash
   git push -u origin feature/menu-improvements
   # The -u flag sets up tracking between local and remote
   ```

4. **Verify on GitHub**:
   - Open your repository on GitHub
   - Click the **branches** dropdown
   - You should see your branch listed!

</details>

---

<details>
<summary><strong>🎯 Task 3: PRs & CI</strong></summary>

> **🧪 Concept pill: Pull requests & CI/CD**
>
> **Pull request (PR):** A formal proposal to merge your changes
> - Shows what changed and why
> - Allows discussion before merging
> - Creates a paper trail of decisions
>
> **Continuous integration (CI):** Automated checks that run on every PR
> - Compiles code
> - Runs tests
> - Checks code quality
> - In Github are run on Github Actions
> - **Should be green before merging!**
>
> **Why CI matters:** Catches problems before they reach main branch. If CI is red, the code has issues that need fixing.
>
> **Review etiquette:** Be kind, specific, and constructive. "Consider..." is better than "Wrong!"

### Part 1: Create PR and observe CI failure

1. **Open a pull request on GitHub**:
   - Go to your repository on GitHub
   - You'll see a yellow banner: "Compare & pull request"
   - Click it (or go to Pull requests → New)
   - Verify: base: `main` ← compare: `feature/menu-improvements`

   **📝 PR Title Best Practices:**
   - Be specific and descriptive (5-10 words)
   - Start with a verb: "Add", "Fix", "Update", "Remove"
   - ✅ Good: "Add Tuesday taco special to daily menu"
   - ❌ Bad: "Updates" or "My changes" or "Task 3"

   **📋 PR Description Template:**
   ```markdown
   ## Summary
   Brief description of what this PR does (1-2 sentences).
   Example: "Adds a new Tuesday special (tacos) to the daily specials menu."

   ## Changes Made
   - Added Tuesday taco special to DailySpecials.java
   - Price set at $8.99

   ## Testing
   - [ ] Code compiles successfully
   - [ ] CI checks pass
   - [ ] Output displays correctly

   ## Notes
   (Any additional context or considerations)
   ```

   **💡 Relationship between commits and PR:**
   - Your PR title summarizes ALL commits in the branch
   - Individual commit messages appear in the "Commits" tab
   - Good commit messages make PR review easier
   - Example: If you have commits "add: Tuesday special" and "fix: price format",
     your PR title could be "Add Tuesday taco special with corrected pricing"

2. **Watch for automated checks** 🤖:
   - Notice the yellow dot turning to a red X
   - Click "Details" to see what failed
   - 🔴 **The CI check failed!** This is expected
   - Read the error message: "SOLD OUT items not allowed"

### Part 2: Fix the CI failure

**Important:** You cannot and should not merge a PR with failing CI checks. Let's fix it:

3. **Fix the issue that's causing CI to fail**:
   ```bash
   # Make sure you're on your feature branch
   git checkout feature/menu-improvements
   # Or: git switch feature/menu-improvements

   # Edit src/DailySpecials.java
   # Remove "SOLD OUT - " from your line
   # Should be: System.out.println("Tuesday: Tacos - $8.99");

   git add src/DailySpecials.java
   git commit -m "fix: remove sold out status to pass CI checks"
   git push
   ```

4. **Verify CI passes** ✅:
   - Return to your PR on GitHub
   - Wait for checks to re-run (~30 seconds)
   - Should now show green checkmark!
   - **DO NOT proceed until CI is green**

### Part 3: Code review and merge

5. **Your task as reviewer** (for teammate's PR):
   - Find your teammate's PR
   - **First check:** Is CI green? If not, request they fix it first
   - Click "Files changed" tab
   - Leave constructive comments
   - Only approve if CI is passing
   - Click "Review changes" → "Approve" → Submit

6. **Merge your PR** (only after CI is green and you have approval):
   - Your PR should show: ✅ All checks have passed
   - Your PR should show: ✅ Approved by [teammate]
   - Click "Merge pull request"
   - Click "Confirm merge"

</details>

---

<details>
<summary><strong>🎯 Task 4: stash & conflict resolution (the final boss)</strong></summary>

> **🧪 Concept pill: Git stash (your safety net)**
>
> **What is stash?** A temporary storage for uncommitted changes
>
> **When to use stash:**
> - Need to switch branches but have uncommitted work
> - Want to pull updates but have local changes
> - Need to quickly test something else without losing current work
>
> **Common stash commands:**
> ```bash
> git stash              # Save current changes
> git stash list         # See all stashes
> git stash pop          # Apply and remove latest stash
> git stash apply        # Apply but keep stash
> git stash drop         # Delete a stash
> ```
>
> **Real-world analogy:** Like putting your desk work in a drawer when someone needs the desk urgently

> **🧪 Concept pill: Merge conflicts**
>
> **What causes conflicts:** Two people change the same line differently
>
> **How Git marks conflicts:**
> ```
> <<<<<<< HEAD
> Your version
> =======
> Their version
> >>>>>>> main
> ```
>
> **Resolution strategy:**
> 1. Don't panic! Conflicts are normal
> 2. Understand both changes
> 3. Combine the best of both
> 4. Remove the conflict markers
> 5. Test that it still works
>
> **Remember:** Conflicts are opportunities to make the code better by combining ideas!

### Part 1: Using stash

**Scenario:** You're working on something but need to switch branches urgently.

1. **Start some work without committing**:
   ```bash
   git checkout main
   git pull origin main
   git checkout -b feature/work-in-progress

   # Edit src/Menu.java
   # Add a comment: // TODO: Add seasonal menu

   # Check status - you have uncommitted changes
   git status
   git diff
   ```

2. **Try to switch branches** (this might fail or warn you):
   ```bash
   git checkout main
   # Git might complain about uncommitted changes!
   ```

3. **Use stash to save your work**:
   ```bash
   # Stash your changes
   git stash save "WIP: seasonal menu notes"

   # Now your working directory is clean
   git status

   # You can safely switch branches
   git checkout main

   # See your stashed work
   git stash list
   ```

4. **Retrieve your stashed work later**:
   ```bash
   git checkout feature/work-in-progress
   git stash pop
   # Your changes are back!
   git diff
   ```

5. **Clean up and return to main before next exercise**:
   ```bash
   # IMPORTANT: Return to main branch before continuing to Part 2
   git checkout main
   git pull origin main  # Make sure you're up to date

   # Delete the temporary branch
   git branch -d feature/work-in-progress
   ```

### Part 2: Creating and resolving a conflict

**Goal:** Intentionally create a merge conflict to learn how to resolve it safely.

> **Note:** Both teammates must start from the SAME commit on main to guarantee a conflict will occur.

#### Step 1: Everyone synchronizes to the same starting point
```bash
# ALL team members run this first:
git checkout main
git pull origin main
git log --oneline -1  # Note this commit hash - everyone should see the same one!
```

#### Step 2: Teammate A creates their change
```bash
# Teammate A ONLY:
git checkout -b feature/changelog-veggie

# Edit CHANGELOG.md
# Find the line: "EDIT THIS LINE FOR CONFLICT"
# Change it to: "Version 1.1.0 - Added vegetarian options"

git add CHANGELOG.md
git commit -m "docs: update changelog for vegetarian menu"
git push -u origin feature/changelog-veggie
```

#### Step 3: Teammate A creates and merges their PR
1. Go to GitHub and create a Pull Request
2. **IMPORTANT:** Set up the PR as: `feature/changelog-veggie` → `main`
3. **PR Title:** "Add vegetarian options to changelog"
4. **PR Description:** "Updates changelog to document new vegetarian menu items"
5. Have another teammate approve it
6. **MERGE the PR immediately** (this is critical for creating the conflict)

#### Step 4: Teammate B creates their conflicting change
```bash
# Teammate B: Start this AFTER A creates their PR but BEFORE refreshing main
# ⚠️ DO NOT run 'git pull' - you need to work from the OLD version of main
# This ensures you'll edit the same line that A just changed, creating a conflict

git checkout main  # Make sure you're on main
# DO NOT PULL! You need the old version
git checkout -b feature/changelog-gluten

# Edit the SAME line in CHANGELOG.md
# It still shows: "EDIT THIS LINE FOR CONFLICT"  (not A's changes)
# Change it to: "Version 1.1.0 - Added gluten-free options"

git add CHANGELOG.md
git commit -m "docs: update changelog for gluten-free menu"
git push -u origin feature/changelog-gluten
```

#### Step 5: Teammate B creates their PR (conflict will appear)
1. Go to GitHub and create a Pull Request
2. **Set up the PR as:** `feature/changelog-gluten` → `main`
3. **PR Title:** "Add gluten-free options to changelog"
4. **⚠️ GitHub will show:** "This branch has conflicts that must be resolved"
5. This is expected! You've successfully created a conflict.

### Part 3: Resolving the conflict (Teammate B)

1. **Notice the conflict in your PR** (you should see the warning on GitHub)

2. **Pull the latest main and merge to resolve**:
   ```bash
   git checkout main
   git pull origin main
   git checkout feature/changelog-gluten
   git merge main
   # Conflict message appears!
   ```

3. **Fix the conflict**:
   ```bash
   # Open CHANGELOG.md in your editor
   # You'll see:
   # <<<<<<< HEAD
   # Version 1.1.0 - Added gluten-free options
   # =======
   # Version 1.1.0 - Added vegetarian options
   # >>>>>>> main

   # Edit to combine both:
   # Version 1.1.0 - Added vegetarian and gluten-free options

   # Save the file, then:
   git add CHANGELOG.md
   git merge --continue
   # Or: git commit -m "fix: merge conflict combining both menu options"
   git push
   ```

4. **Verify on GitHub**:
   - Your PR should now be mergeable
   - The conflict warning is gone!

</details>

---

<details>
<summary><strong>🎯 Task 5: Revert safely (the time machine)</strong></summary>

> **🧪 Concept pill: Reverting changes**
>
> **What reverting does:** Creates a NEW commit that undoes a previous commit
>
> **Why not just delete?**
> - History is sacred (shows what happened and why)
> - Others might have based work on it
> - You can revert the revert if needed!
>
> **When to revert:**
> - Feature causes unexpected problems
> - Wrong change was merged
> - Temporary rollback needed
>
> **Pro tip:** Reverting is always safe. Deleting history is dangerous!

### Your task:

1. **Find a merged PR to revert**:
   - Go to Pull requests → Closed
   - Pick any recently merged PR
   - Click to open it

2. **Create a revert**:
   - Click the **Revert** button
   - GitHub automatically creates a revert PR
   - Notice the title: "Revert [original PR title]"

3. **Review what's happening**:
   - Click "Files changed"
   - See how it undoes the original changes
   - Everything that was added is removed
   - Everything that was removed is added back

4. **Complete the revert**:
   - Have a teammate approve it
   - Merge the revert PR
   - Check the main branch (the change is gone but history remains!)

</details>

---

<details>
<summary><strong>🎯 Task 6: Tags and releases (marking milestones)</strong></summary>

> **🧪 Concept pill: Git tags**
>
> **What are tags?** Permanent markers for specific points in history
>
> **Two types of tags:**
> - **Lightweight tags:** Just a pointer to a commit (like a bookmark)
> - **Annotated tags:** Full objects with author, date, and message (like a certificate)
>
> **Why use tags?**
> - Mark release versions (v1.0, v2.0)
> - Identify important milestones
> - Easy reference points for deployment
> - Create GitHub releases for downloads
>
> **Semantic versioning:** MAJOR.MINOR.PATCH (e.g., 2.1.3)
> - MAJOR: Breaking changes
> - MINOR: New features, backwards compatible
> - PATCH: Bug fixes

### Your task:

1. **Ensure you're on the latest main branch**:
   ```bash
   git checkout main
   git pull origin main
   ```

2. **Create your first annotated tag**:
   ```bash
   # Create annotated tag with message
   git tag -a v1.0.0 -m "Release version 1.0.0 - Initial cafeteria menu system"

   # View the tag details
   git show v1.0.0

   # List all tags
   git tag -l
   ```

3. **Push the tag to GitHub**:
   ```bash
   # Push specific tag
   git push origin v1.0.0

   # Or push all tags
   git push origin --tags
   ```

4. **Create a GitHub release**:
   - Go to your repository on GitHub
   - Click on **Releases** (right side of page)
   - Click **Create a new release**
   - Choose your tag: v1.0.0
   - Release title: "Version 1.0.0 - Cafeteria Menu System"
   - Description: Add release notes describing what's included
   - Click **Publish release**

5. **Simulate a patch release** (bug fix):
   ```bash
   # Create a quick fix branch
   git checkout -b fix/price-format

   # Make a small change (fix a price format or add a comment)
   # Edit any file with a price, ensure format is $X.XX

   git add .
   git commit -m "fix: correct price format for consistency"
   git push -u origin fix/price-format
   ```

6. **After merging the fix, create a patch version**:
   ```bash
   # After PR is merged
   git checkout main
   git pull origin main

   # Create patch version tag
   git tag -a v1.0.1 -m "Patch: Fixed price formatting"
   git push origin v1.0.1
   ```

</details>

---

<details>
<summary><strong>🎯 Task 7: Fetch vs pull (understanding the difference)</strong></summary>

> **🧪 Concept pill: Fetch vs pull**
>
> **git fetch:** Downloads changes but doesn't merge them
> - Safe to run anytime
> - Lets you review changes before integrating
> - Updates remote-tracking branches (origin/main)
>
> **git pull:** Fetches AND merges in one command
> - Equals: `git fetch` + `git merge`
> - Can cause unexpected merge commits
> - Convenient but less control
>
> **When to use each:**
> - **fetch:** When you want to see what's new without changing your files
> - **pull:** When you're ready to integrate remote changes immediately
>
> **Pro workflow:**
> ```bash
> git fetch origin           # See what's new
> git log HEAD..origin/main  # Review changes
> git merge origin/main      # Integrate when ready
> ```

### Practice exercise:

1. **See the difference in action**:
   ```bash
   # First, use fetch to see remote changes
   git fetch origin
   git status
   # Shows: "Your branch is behind origin/main by X commits"

   # Review what's new without merging
   git log HEAD..origin/main --oneline

   # When ready, merge the changes
   git merge origin/main
   ```

</details>

---

## 🎉 Wrap up: Team reflection

### Generate your Git history:
```bash
git checkout main
git pull origin main

# See your beautiful history!
git log --oneline --graph --decorate --all -20
```

### Create a team reflection issue:

1. **Create a new blank issue** titled: "🏆 Team Lab Completed!"
2. **Paste your git log output** in a code block
3. **Answer together:**
   - Which was your previous Git level? Did you use it?
   - Which concept clicked first? Which took longer?
   - Which Git feature do you consider to be the most useful?
   - Which task was the most challenging and why?
   - Try to find the code for the Github Action workflow we used as CI, do you understand it?

---

### 📤 Lab submission

Lab submission will be done through the PDU. Add your forked repository URL to the task.

---

## 🛡️ Git command cheatsheet

### Essential commands

```bash
# Basics
git status              # What's changed?
git diff                # Show changes
git log --oneline       # History

# Working with changes
git add <file>          # Stage file
git add .               # Stage all
git commit -m "msg"     # Commit
git reset <file>        # Unstage

# Branches
git branch              # List branches
git checkout -b <name>  # Create & switch
git switch <branch>     # Switch (new way)
git merge <branch>      # Merge

# Remote
git clone <url>         # Copy repo
git pull                # Get changes
git push                # Send changes
git fetch               # Check updates

# Stash
git stash               # Save work
git stash pop           # Retrieve work

# Fix problems
git merge --abort       # Cancel merge
git reset --hard HEAD~1 # Undo commit
git revert <commit>     # Reverse commit
```

### Pro tips
- Use `git status` constantly
- Commit often with good messages
- Always work on branches
- Pull before starting work
- Push when work is complete

---

## 💭 Final thoughts

Congratulations! You've just completed real-world Git workflows that professional developers use every day. Remember:

- **Git is about collaboration**, not perfection
- **Mistakes are learning opportunities** (you can always revert!)
- **Practice makes permanent** (use Git in your next project)
- **Command line mastery** gives you power and speed

---

*May your commits be atomic and your merges be clean!* 🚀
