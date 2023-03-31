if(firstSync){
//issue.repository   = "vkarve-chef/sync-sandbox"
return // ignore new issues
}
issue.summary      = replica.summary
issue.description  = replica.description
//issue.comments     = commentHelper.mergeComments(issue, replica)

combineComments()

def combineComments() {
    replica.comments.each { comment ->
        boolean isAlreadyPresent = false
        issue.comments.each { destnComment ->
            if ( comment.body == destnComment.body ) {
                isAlreadyPresent = true
            }
        }
        if ( !isAlreadyPresent ) {
            issue.comments.add(comment)
        }
    }
}

/*
Status Synchronization

Sync status according to the mapping [remote issue status: local issue status]
If statuses are the same on both sides don"t include them in the mapping
*/
def statusMapping = ["To Do":"open", "In Progress":"open", "Done": "closed"]
def remoteStatusName = replica.status.name
issue.setStatus(statusMapping[remoteStatusName] ?: remoteStatusName)

/*
issue.labels       = replica.labels
issue.assignee     = nodeHelper.getUserByUsername(replica.assignee?.username)
issue.reporter     = nodeHelper.getUserByUsername(replica.reporter?.username)
*/