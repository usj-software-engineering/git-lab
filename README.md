# Lab 01: Version control with Git

This lab statement is part of the course **Software Engineering** at Universidad San Jorge, Zaragoza. All contents are original.

> **Note**: this lab counts as theoretical content. Everything in the boxes below, and every command you run here, may be asked in the January exam.

This is a team lab for 3 to 4 people. You will run a real team workflow on a university cafeteria menu, with branch protection, pull requests, automated checks and a merge conflict that you will have to resolve.

## Team members

| Name | GitHub username |
|------|-----------------|
| | |
| | |
| | |
| | |

## Lab rules

1. Never push directly to `master`. Every change goes through a pull request.
2. Every pull request needs one approval from a teammate.
3. Automated checks must pass before merging.
4. Keep commits small. One logical change per commit.
5. Write commit messages that say why, following the conventions in Task 1.

Your grade comes from how well the repository reflects these rules.

### Use the terminal

Run this lab with Git commands from the terminal:

- ❌ No IDE Git panels (VS Code, IntelliJ)
- ❌ No desktop clients (SourceTree, GitKraken, GitHub Desktop)
- ✅ Git commands you type yourself

Visual tools are common in industry and you will use them later. The reason to start here is that the commands are what everything else is built on: when a tool does something strange, or when you are on a server with no interface, the terminal is all you have.

### How you will merge

The merge button on GitHub offers three options. Use **Squash and merge** throughout this lab, the same one we used in class: the whole branch is folded into a single commit on `master`, so the history reads as a list of finished tasks rather than a tangle of work in progress.

One side effect to expect. Squashing creates a new commit, so your original commits never literally reach `master`, and `git branch -d` will refuse to delete the branch with a "not fully merged" warning. Once the pull request is merged, `git branch -D` is the right answer.

## Learning goals

By the end of the lab you will be able to:

- Make clean commits with messages that follow a convention
- Work with branches and pull requests the way a team does
- Read a diff and review a teammate's work
- Resolve a merge conflict without losing anyone's changes
- Keep secrets out of a repository with `.gitignore`
- Park unfinished work with `git stash`
- Undo a merged change with a revert
- Mark a release with a tag

---

## Pre-lab: repository setup

Before you start, you need Git installed and a GitHub account. You do not need Java: the automated checks compile the code for you on GitHub. Install it only if you want to run the menu on your own machine.

**On Windows, use Git Bash**, which comes with Git for Windows. The Git commands themselves work the same in PowerShell, but everything else about this lab assumes a Unix-style shell, and PowerShell and CMD each have their own way of doing the simplest things. Git Bash gives you the same terminal your classmates on macOS and Linux are using.

Whenever the lab asks you to create or change a file, do it in your editor. Creating files by typing into the terminal is a good way to end up with quotes inside your file or an encoding nobody expected.

**1. Fork the repository.** Only one team member does this.

- Open the [original repository](https://github.com/usj-software-engineering/git-lab)
- Click **Fork**, top right, and select your own account
- Once the fork exists, go to **Actions** and click **I understand my workflows, go ahead and enable them**

**2. Add your teammates as collaborators.** Same person, in the fork.

- **Settings** → **Collaborators** → **Add people**
- Add each teammate by GitHub username. They get an email invitation they need to accept.
- Under **Settings** → **General**, tick **Issues** so the feature is available

**3. Everyone clones the fork.**

```bash
git clone https://github.com/<owner-username>/git-lab.git
cd git-lab
```

Use the username of whoever created the fork, not your own.

The default branch here is called `master`, the same one we used in class. GitHub has created new repositories with `master` renamed to `main` since 2020, so you will run into both names. They are the same thing: the branch everything else merges into. Git treats neither name as special, it is just what the repository was set up with.

**4. Everyone configures their identity.**

Git stamps every commit with a name and an email. Set yours once on your laptop and every repository you work on from now on will use it:

```bash
git config --global user.name "Your name"
git config --global user.email "your.email@alu.usj.es"

git config --global --list
```

If you have used Git before, this is probably already set. Run the last command first and check.

Use the email that is on your GitHub account, or GitHub will not link your commits to your profile and they will show up as an anonymous author. You can add your university address to your GitHub account if you have not already.

There is also `git config --local`, which writes the setting into one repository instead of your whole machine. That is how people keep a work identity on work repositories and a personal one everywhere else.

**5. Fill in the team table.** One person only, and this is the only change that goes straight to `master`.

```bash
# Edit the "Team members" table in README.md, then:
git add README.md
git commit -m "docs: add team members"
git push origin master
```

**6. Protect the `master` branch.** The person who owns the fork.

- **Settings** → **Rules** → **Rulesets** → **New branch ruleset**
- Name: `Main branch protection`
- **Enforcement status**: Active
- **Target branches** → **Add target** → **Include by pattern** → type `master`
- Tick these rules:
  - Require a pull request before merging
  - Require approvals: 1
  - Dismiss stale pull request approvals when new commits are pushed
  - Block force pushes
  - Require status checks to pass
    - Click **Add checks** and search for **Check Menu Quality**. If it does not appear, the workflow has not run yet. Open **Actions**, run it once, and come back.
- Click **Create**

Leave the bypass list empty. If the owner can still push to `master`, the protection is not doing its job.

---

## Tasks

A branch name describes the task, never the person: your name is already on every commit. To keep four people from pushing branches on top of each other, **each of you picks a different dish** in Task 1 and carries it through the lab. That dish names your branches and is what you add to the menu, so no two are alike.

The examples below use `pizza`. Use yours.

<details>
<summary><strong>Step 0: check that the protection works</strong></summary>

> **Concept pill: protected branches**
>
> A branch ruleset tells GitHub to refuse certain operations on certain branches. The usual target is the branch that goes to production, so that nothing lands on it without review.
>
> Protection lives on the server, not in your clone. Git on your laptop will happily let you commit to `master`; the refusal arrives when you push. That is why the error below comes from `git push` and not from `git commit`.

**1. Try to push straight to `master`.** This must fail.

Open `README.md` in your editor, add any line at the end, save, and then:

```bash
git add README.md
git commit -m "test: verify branch protection"
git push origin master
```

**2. Copy the error message** into a scratch file on your computer. You will paste it into the reflection issue at the end of the lab.

**3. Undo the local commit** so your clone matches the server again.

```bash
git reset --hard origin/master
git log --oneline -1   # same commit as on GitHub
```

`reset --hard` throws away the commit and the change to the file. It is safe here because nothing was pushed and nobody else has seen it. Be careful with it anywhere else.

</details>

---

<details>
<summary><strong>Task 1: your first commits</strong></summary>

> **Refresher: the three stages**
> *Seen in class: Git slides, "The three stages of a change".*
>
> working directory → `git add` → staging area → `git commit` → repository.
> `git status` tells you where everything is. `git diff` shows what you are about to stage.

> **Refresher: conventional commits**
> *Seen in class: Git slides, "Conventional commits".*
>
> A message explains why the change exists. The diff already shows what changed. Prefix it with its type:

| Prefix | Purpose | Example |
|--------|---------|---------|
| `feat:` | New functionality | `feat: add room availability calendar` |
| `fix:` | A bug | `fix: prevent double booking on simultaneous confirms` |
| `docs:` | Documentation | `docs: explain how to run the project locally` |
| `refactor:` | Same behaviour, better code | `refactor: extract price calculation from BookingService` |
| `test:` | Tests | `test: cover bookings that cross midnight` |
| `chore:` | Tooling, dependencies | `chore: update dependencies` |

These are the six from the slides. The [specification](https://www.conventionalcommits.org) has more, and some teams invent their own, but stick to these six here so everybody's history reads the same way.

The good and bad examples are in the Git slides, "Writing a good commit message". Keep the table above open while you work.

**1. Start from a clean, up-to-date `master`.**

```bash
git switch master
git pull origin master
git status   # should say "nothing to commit, working tree clean"
```

**2. Pick your dish and branch for it.** Agree within the team so no two people take the same one. Every task gets a branch, and `master` is protected anyway.

```bash
git switch -c feature/pizza
```

**3. Add a food item.** Open `src/Food.java` and add a line **inside** the `display()` method, next to the ones already there:

```java
System.out.println("Pizza - $8.99");
```

The closing `}` of the method must stay below your line. The automated check compiles this file, so a line in the wrong place will fail later.

```bash
git diff
git add -p src/Food.java
```

`git add -p` walks you through the change one block at a time and asks what to do with each. Press `y` to stage the block, `n` to skip it, `q` to quit. It is the habit that stops you committing a stray debug line you forgot about.

```bash
git commit -m "<write your own message, with a prefix from the table>"
```

**4. Add a drink to go with it.** Same idea in `src/Drinks.java`, inside `display()`. Pick one nobody else has taken:

```java
System.out.println("Lemonade - $3.50");
```

```bash
git diff
git add src/Drinks.java
git commit -m "<write your own message>"
```

**5. Look at what you have built.**

```bash
git log --oneline -5
```

Two commits, each with one change and a message that explains it. Nothing has left your computer yet.

</details>

---

<details>
<summary><strong>Task 2: keeping secrets out</strong></summary>

> **Refresher: `.gitignore`**
> *Seen in class: Git slides, "Good practices".*
>
> One path pattern per line, listing what Git must never track: secrets (`.env`, `credentials.txt`), build output (`*.class`, `target/`), dependencies (`node_modules/`), editor and OS noise (`.DS_Store`).
>
> **The part that bites:** `.gitignore` only stops files that are not tracked yet. Once a secret is committed it stays in the history forever, and the only real fix is to change the password. You are about to see how close that gets.

Stay on your branch from Task 1.

**1. Create a file with fake credentials.**

In your editor, create `src/credentials.txt` with these two lines:

```
database_password=cafeteria123
api_key=SECRET_KEY_DO_NOT_SHARE
```

Nothing here is a real secret, but treat it as though it were.

**2. Look at what Git thinks of it.**

```bash
git status
```

It shows up as untracked. Git has noticed it and is waiting for you to decide.

**3. Stage everything in one go,** the way people do when they are in a hurry:

```bash
git add .
git status
```

`credentials.txt` is now staged, ready to be committed. One `git commit` and one `git push` away from being public.

**4. Unstage it before that happens.**

```bash
git restore --staged src/credentials.txt
git status
```

Back to untracked. Nothing was committed, so nothing is in the history.

**5. Tell Git to ignore it for good.**

Create a file called `.gitignore` in the root of the repository, with these two lines:

```
src/credentials.txt
*.log
```

The name starts with a dot and has no extension. Some file explorers hide files like this; your editor will not.

```bash
git status
```

`credentials.txt` has disappeared from the output. Git is still ignoring it, not hiding it: the file is still on your disk.

**6. Commit the `.gitignore`.**

```bash
git add .gitignore
git commit -m "chore: ignore credentials and log files"
```

**7. Check that the rule works.**

Create an empty file called `debug.log` anywhere in the repository, then:

```bash
git status
```

`debug.log` does not appear, because `*.log` covers it. Git is ignoring a file it has never seen before, which is the point: the rule is about the pattern, not about that one file.

Delete `debug.log` and `src/credentials.txt` from your editor or file explorer when you are done looking at them.

</details>

---

<details>
<summary><strong>Task 3: push your branch</strong></summary>

> **Refresher: branches and remotes**
> *Seen in class: Git slides, "Why branches exist" and "Ana, Bruno and GitHub: three full repos".*
>
> `master` is stable and protected. Every task gets its own branch, named `type/what`.
> `origin` is the copy on GitHub. Your clone is a full repository: you commit locally, then `push` to share and `pull` to catch up.

> **Concept pill: why two commands do the same thing**
>
> The slides used `git switch`. You will meet `git checkout` everywhere: in older codebases, in answers written before 2019, and in your teammates' fingers.
>
> ```bash
> git checkout -b new-branch      # older form: create and switch
> git switch -c new-branch        # since Git 2.23, same result
> ```
>
> `checkout` used to do both jobs: it switched branches and it restored files. A typo in the second job could silently overwrite work you had not committed. Git split it into `git switch` for moving between branches and `git restore` for discarding changes, so the dangerous operation has its own name.
>
> Use whichever you prefer. Recognise both when you read them.

**1. Add a daily special.** Open `src/DailySpecials.java` and add this line inside `display()`, exactly as written:

```java
System.out.println("Tuesday: SOLD OUT - Tacos - $8.99");
```

```bash
git add src/DailySpecials.java
git commit -m "feat: add Tuesday taco special"
```

**2. Push your branch.**

```bash
git push -u origin feature/pizza
```

`-u` links your local branch to the one on GitHub, so later you can just type `git push`.

**3. Check it arrived.** Open the repository on GitHub and look in the branches dropdown. Your branch is there, and `master` is untouched.

</details>

---

<details>
<summary><strong>Task 4: pull requests and automated checks</strong></summary>

> **Refresher: pull requests**
> *Seen in class: Git slides, "What a pull request is" and "The workflow".*
>
> A pull request is a formal proposal to merge your branch, along with the discussion and the record that come with it. Nothing reaches `master` any other way.
>
> When you review, be specific and be kind. "Consider extracting this into a method" is useful. "Wrong" is not.

> **Concept pill: continuous integration**
>
> Continuous integration is a machine that checks out your branch and runs a script every time you push. On GitHub it is called GitHub Actions and it is configured by a YAML file in `.github/workflows/`.
>
> It usually compiles the code, runs the tests and checks style. Some projects also deploy from it.
>
> The reason it exists is that "it works on my machine" is not evidence. CI runs on a clean machine that has never seen your laptop, so if it builds there it builds for everybody.
>
> On a pull request you get a yellow dot while it runs, then a green tick or a red cross. A red cross blocks the merge, which is the whole point: problems get caught before a human spends time reading the code.
>
> This repository has a workflow. You have not read it yet. You will at the end of the lab.

### Part 1: open the pull request

**1. Open it on GitHub.**

- Go to the repository. A banner offers **Compare & pull request** for the branch you just pushed.
- Check the direction: base `master` ← compare `feature/pizza`.
- Title it in 5 to 10 words, starting with a verb. "Add Tuesday taco special to daily menu" works. "Updates", "My changes" and "Task 4" do not.
- The description template fills itself in. Complete it.

**2. Watch the checks run,** then read what happens.

Click **Details** next to the check and read the log. Work out which step failed and why before you go on.

### Part 2: make the check pass

**3. Fix the cause.** Edit `src/DailySpecials.java` so the line reads:

```java
System.out.println("Tuesday: Tacos - $8.99");
```

```bash
git add src/DailySpecials.java
git commit -m "fix: remove sold out marker from Tuesday special"
git push
```

Note that you did not open a new pull request. Pushing to the same branch updates the existing one.

**4. Wait for the green tick.** It takes around 30 seconds. Do not continue until you have it.

### Part 3: review and merge

**5. Review a teammate's pull request.**

- Check the automated result first. If it is red, say so and let them fix it before you spend time reading.
- Open **Files changed** and read the diff.
- Leave at least one comment that is about the change itself, not about whether it exists.
- **Review changes** → **Approve** → **Submit**.

**6. Merge yours** once it has a green tick and an approval.

- **Merge pull request** → **Squash and merge** → **Confirm squash and merge**
- Delete the branch when GitHub offers to, then on your laptop with `git branch -D feature/pizza`

</details>

---

<details>
<summary><strong>Task 5: parking unfinished work with stash</strong></summary>

> **Concept pill: `git stash`**
>
> `git stash` takes everything you have changed but not committed, saves it on a stack, and leaves your working directory clean. `git stash pop` brings it back.
>
> ```bash
> git stash push -m "message"   # save what you have, clean the directory
> git stash list                # what is on the stack
> git stash pop                 # bring back the most recent one and remove it
> git stash apply               # bring it back and keep it on the stack
> git stash drop                # throw one away
> ```
>
> The reason it exists is not that Git stops you from switching branches. Usually it does not: your uncommitted changes simply come with you, which is the actual problem. Stash is how you deliberately put work down before touching something else, so that it is waiting where you left it.

**1. Start something and leave it unfinished.**

```bash
git switch master
git pull origin master
git switch -c feature/seasonal-menu
```

Add a comment near the top of `src/Menu.java`:

```java
// TODO: seasonal menu
```

```bash
git status
```

**2. Switch to `master` without doing anything about it.**

```bash
git switch master
git status
```

Look carefully: Git let you switch, and your edit to `Menu.java` came with you. It is now sitting on `master`, where you did not want it. This is what stash prevents.

**3. Go back and put the work down properly.**

```bash
git switch feature/seasonal-menu
git stash push -m "WIP: seasonal menu notes"
git status
git stash list
```

The working directory is clean and your change is on the stack.

**4. Switch away and come back.**

```bash
git switch master
git status          # clean, nothing followed you this time
git switch feature/seasonal-menu
git stash pop
git diff            # your TODO is back
```

**5. Clean up.** This branch was only an exercise, so throw the change away and delete it.

```bash
git restore src/Menu.java
git switch master
git branch -d feature/seasonal-menu
git status          # clean
```

Leaving a modified `Menu.java` behind here will follow you into the next task and end up in a pull request where it does not belong.

</details>

---

<details>
<summary><strong>Task 6: creating and resolving a conflict</strong></summary>

> **Refresher: merge conflicts**
> *Seen in class: Git slides, "Anatomy of a conflict".*
>
> Two people changed the same line, so Git stops and asks you. It marks the file like this:
>
> ```
> <<<<<<< HEAD
> Your version
> =======
> Their version
> >>>>>>> master
> ```
>
> Read both, write the version that should exist, delete the three marker lines, `git add`, and continue. The markers are ordinary text: if you leave one behind, it ships.

This task needs two people, A and B, working at the same time. Agree on who is who and sit together, or get on a call. If B updates their `master` at the wrong moment there is no conflict and the exercise does not work.

The rest of the team watches, reviews and approves.

**Step 1: everyone starts from the same commit.**

```bash
git switch master
git pull origin master
git log --oneline -1   # A and B must see the same commit here
```

**Step 2: A makes a change.**

```bash
# A only
git switch -c docs/changelog-veggie
```

In `CHANGELOG.md`, find the line marked as the conflict line and replace it with:

```
## [Unreleased] Version 1.1.0 - Added vegetarian options
```

```bash
git add CHANGELOG.md
git commit -m "docs: record vegetarian options in changelog"
git push -u origin docs/changelog-veggie
```

**Step 3: A opens the pull request and merges it.**

Title it "Add vegetarian options to changelog", get a teammate to approve it, and squash and merge it. `master` now contains A's line.

**Step 4: B makes a conflicting change.**

B has not run `git pull` since Step 1, so B's `master` is the old one. That is what makes this work.

```bash
# B only. Do not pull.
git switch master
git switch -c docs/changelog-gluten
```

In `CHANGELOG.md`, the conflict line still shows the original text, because B has not seen A's merge. Replace that same line with:

```
## [Unreleased] Version 1.1.0 - Added gluten-free options
```

```bash
git add CHANGELOG.md
git commit -m "docs: record gluten-free options in changelog"
git push -u origin docs/changelog-gluten
```

**Step 5: B opens a pull request.**

GitHub shows "This branch has conflicts that must be resolved". Two people changed the same line and Git will not guess which one wins.

**Step 6: B resolves it.**

```bash
git switch master
git pull origin master          # now B gets A's change
git switch docs/changelog-gluten
git merge master
```

Git stops and reports the conflict. Open `CHANGELOG.md` and you will find:

```
<<<<<<< HEAD
## [Unreleased] Version 1.1.0 - Added gluten-free options
=======
## [Unreleased] Version 1.1.0 - Added vegetarian options
>>>>>>> master
```

Neither change is wrong, so keep both. Replace all five lines, markers included, with:

```
## [Unreleased] Version 1.1.0 - Added vegetarian and gluten-free options
```

```bash
git add CHANGELOG.md
git commit -m "fix: combine vegetarian and gluten-free changelog entries"
git push
```

> `git merge --continue` does the same thing, but it opens your default editor to write the message. On most machines that is `vim`, and if you have never used it you will not be able to get out. Press `Esc`, then type `:q!` and Enter. The `git commit -m` above avoids the problem entirely.

**Step 7:** the warning on the pull request is gone. Get it approved and merge it. Check `CHANGELOG.md` on `master`: both changes survived.

</details>

---

<details>
<summary><strong>Task 7: undoing a merged change</strong></summary>

> **Concept pill: revert**
>
> A revert creates a **new** commit that undoes what an earlier commit did. The original stays in the history.
>
> That matters because history is shared. Once something is pushed, other people have it, and branches may be built on top of it. Deleting it from under them causes far more trouble than the original mistake. A revert is an ordinary commit going the other way, so everybody receives it through a normal `pull`.
>
> It is also reversible: you can revert a revert.

**1. Find your own pull request from Task 4,** the one with the taco special. **Pull requests** → **Closed**.

**2. Click Revert.** GitHub creates a branch and opens a new pull request titled "Revert ...".

**3. Read it before merging.** Open **Files changed**. Every line the original added is removed, and anything it removed comes back.

**4. Get it approved and merge it.**

**5. Look at what happened.**

```bash
git switch master
git pull origin master
git log --oneline -5
```

The taco line is gone from `src/DailySpecials.java`, and both the commit that added it and the commit that removed it are in the log. Nothing was erased.

</details>

---

<details>
<summary><strong>Task 8: tags and releases</strong></summary>

> **Concept pill: tags**
>
> A tag is a permanent name for one commit. Branches move as you commit; tags do not. They are how a project says "this exact commit is version 1.0.0".
>
> There are two kinds. A **lightweight** tag is just a name pointing at a commit. An **annotated** tag is a real object with an author, a date and a message, which is what releases should use.
>
> **Semantic versioning** numbers them MAJOR.MINOR.PATCH:
> - MAJOR: changes that break existing users
> - MINOR: new features that do not break anything
> - PATCH: bug fixes

**1. Get up to date.**

```bash
git switch master
git pull origin master
```

**2. Create an annotated tag.**

```bash
git tag -a v1.0.0 -m "Initial cafeteria menu system"
git show v1.0.0
git tag -l
```

`git show` prints the tag's own message and author above the commit it points at. Compare that with `git tag v0.0.1-test` and `git show v0.0.1-test`, which has none of it, and then delete the test one with `git tag -d v0.0.1-test`.

**3. Push the tag.** Tags are not pushed by `git push` on their own.

```bash
git push origin v1.0.0
```

**4. Publish a release.**

- **Releases** on the right of the repository page → **Create a new release**
- Choose the existing tag `v1.0.0`
- Title: "Version 1.0.0"
- Write a short description of what the menu system includes
- **Publish release**

**5. Ship a patch.** Create a branch, fix any price that is not in `$X.XX` format (or add a missing one), and take it through a pull request as usual.

```bash
git switch -c fix/pizza-price     # name it after the price you are fixing
# edit a file
git add src/
git commit -m "fix: use consistent price format for pizza"
git push -u origin fix/pizza-price
```

Once it is approved and merged:

```bash
git switch master
git pull origin master
git tag -a v1.0.1 -m "Fix price formatting"
git push origin v1.0.1
```

A bug fix moves the PATCH number. Nothing broke and nothing new appeared, so MAJOR and MINOR stay.

</details>

---

<details>
<summary><strong>Task 9: fetch and pull</strong></summary>

> **Concept pill: fetch vs pull**
>
> `git fetch` downloads what is new on the server and updates your `origin/master`, without touching your files or your branch. It is safe to run at any time.
>
> `git pull` does the same download and then merges the result into your branch. It is `git fetch` followed by `git merge`.
>
> The difference matters when you want to know what is coming before it arrives, which is most of the time on a busy repository:
>
> ```bash
> git fetch origin
> git log HEAD..origin/master --oneline   # what the server has that you do not
> git merge origin/master                 # take it when you are ready
> ```

This one needs something new on the server that you do not have yet, so coordinate with the rest of the team.

**1. Make sure you are behind.** Have a teammate merge one of their pull requests, or use the release fix from Task 8 if it was merged by somebody else. Do not pull.

**2. Confirm your clone has not noticed yet.**

```bash
git switch master
git status
```

Git reports your branch as up to date, because it is comparing against the `origin/master` it last downloaded, which is now stale.

**3. Download without merging.**

```bash
git fetch origin
git status
```

Now it says you are behind by some number of commits. Nothing in your working directory changed.

**4. Inspect before integrating.**

```bash
git log HEAD..origin/master --oneline
git diff HEAD origin/master
```

**5. Integrate when you are ready.**

```bash
git merge origin/master
```

</details>

---

## Wrap up

**1. Look at the history you have built.**

```bash
git switch master
git pull origin master
git log --oneline --graph --decorate --all -20
git shortlog -sn
```

The graph shows the branches and merges. `git shortlog -sn` counts commits per author.

**2. Open an issue** titled "Team lab completed" and include:

- The output of both commands above, in a code block
- The error message you saved in Step 0
- Your answers to these, agreed as a team:
  - How much Git did each of you know before this lab, and where had you used it?
  - Which idea landed first, and which one took longest?
  - Which of these features do you expect to use most often?
  - Which task gave you the most trouble, and what fixed it?
  - Open `.github/workflows/menu-check.yml`. It is the workflow that has been checking your pull requests. Read it and explain, in your own words, what each step does.

## Submission

Submit through the PDU: add the URL of your forked repository to the task.

Keep the fork public and do not delete it until grades are published.

---

## Command reference

```bash
# Where am I
git status              # what has changed
git diff                # the changes themselves
git log --oneline       # history, one line per commit

# Recording changes
git add <file>          # stage a file
git add -p <file>       # stage a file block by block
git commit -m "msg"     # commit what is staged
git restore --staged <file>   # unstage, keep the change
git restore <file>      # discard the change (cannot be undone)

# Branches
git branch              # list
git switch -c <name>    # create and switch
git switch <name>       # switch
git merge <branch>      # merge a branch into this one

# Sharing
git clone <url>         # copy a repository
git fetch               # download without merging
git pull                # download and merge
git push                # upload
git push -u origin <branch>   # upload and start tracking

# Parking work
git stash push -m "msg" # put changes aside
git stash pop           # take them back

# Undoing
git merge --abort       # abandon a merge in progress
git revert <commit>     # new commit undoing an old one
```

Two habits worth keeping: run `git status` whenever you are unsure, and `git pull` before you start rather than after you finish.

Commands that rewrite or discard history, such as `git reset --hard` and `git push --force`, are not in this list on purpose. They are useful and they are how people lose an afternoon of work. Learn them when you need them, and read what they do first.
