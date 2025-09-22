---
name: Deployment Issue
about: Report issues with GitHub Actions deployment workflows
title: '[DEPLOYMENT] '
labels: ['deployment', 'bug']
assignees: ''
---

## Deployment Issue Report

### Environment
- [ ] Production (`main` branch)
- [ ] Development (`dev`/`develop` branch)
- [ ] Manual workflow trigger

### Workflow Information
- **Workflow Run URL**: [Paste GitHub Actions workflow run URL here]
- **Commit SHA**: [Paste commit hash that triggered the deployment]
- **Branch**: [Branch name]
- **Triggered by**: [Push/PR/Manual]

### Issue Description
**Brief description of the issue:**
[Describe what went wrong during deployment]

### Expected Behavior
**What should have happened:**
[Describe the expected deployment outcome]

### Actual Behavior
**What actually happened:**
[Describe what actually occurred]

### Error Information

#### Workflow Logs
```
[Paste relevant error logs from GitHub Actions]
```

#### Service Logs (if applicable)
```
[Paste logs from DigitalOcean droplet if available]
```

### Deployment Step Where Issue Occurred
- [ ] Repository setup/cloning
- [ ] Maven build/compilation
- [ ] Docker image build
- [ ] Docker image push to registry
- [ ] DigitalOcean deployment
- [ ] Health checks
- [ ] Post-deployment verification

### Environment Configuration
- [ ] I have verified all required GitHub secrets are set
- [ ] I have confirmed DigitalOcean droplet is accessible
- [ ] I have checked that Docker registry credentials are valid
- [ ] SSH keys are properly configured

### Additional Context
**Screenshots (if applicable):**
[Add screenshots of error messages or workflow status]

**Related Issues:**
[Link any related issues or PRs]

**Additional information:**
[Any other context about the problem]

### Debugging Steps Taken
- [ ] Checked workflow logs for errors
- [ ] Verified service health endpoints
- [ ] Checked DigitalOcean droplet status
- [ ] Reviewed environment configuration
- [ ] Attempted manual deployment
- [ ] Other: [describe additional steps]

### Impact
- [ ] Production services are down
- [ ] Development environment is affected
- [ ] Feature deployment is blocked
- [ ] Minor inconvenience

### Priority
- [ ] Critical (production down)
- [ ] High (blocking development)
- [ ] Medium (affecting workflow)
- [ ] Low (cosmetic/improvement)

---

**For urgent production issues, also contact the operations team directly.**