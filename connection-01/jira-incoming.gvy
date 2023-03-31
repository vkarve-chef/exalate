if ( firstSync ) {

   issue.projectKey   = "SEA" 
   // Set type name from source issue, if not found set a default
   if ( replica.type.name == "Pull Request" ) {
      issue.typeName = "Story"
   } else {
      issue.typeName = nodeHelper.getIssueType(replica.type?.name, issue.projectKey)?.name ?: "Bug"
   }

   issue.components = [ nodeHelper.createComponent(issue, "Prod-A", "", null, "PROJECT_DEFAULT") ]
}
issue.summary      = replica.summary
issue.description  = replica.description
// issue.comments     = commentHelper.mergeComments(issue, replica)
issue.attachments  = attachmentHelper.mergeAttachments(issue, replica)
//issue.labels       = issue.labels.addAll(replica.labels) as Set


combineLabels()

def combineLabels() {
    replica.labels.each { label ->
        boolean isAlreadyPresent = false
        issue.labels.each { destnLabel ->
            if ( label.label == destnLabel.label ) {
                isAlreadyPresent = true
            }
        }
        if ( !isAlreadyPresent ) {
            label.label = label.label.replace(" ", "_")
            issue.labels.add(label)
        }
    }
}

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
User Synchronization (Assignee/Reporter)

Set a Reporter/Assignee from the source side, if the user can't be found set a default user
You can use this approach for custom fields of type User
def defaultUser = nodeHelper.getUserByEmail("default@idalko.com")
issue.reporter = nodeHelper.getUserByEmail(replica.reporter?.email) ?: defaultUser
issue.assignee = nodeHelper.getUserByEmail(replica.assignee?.email) ?: defaultUser
*/

/*
Comment Synchronization

Sync comments with the original author if the user exists in the local instance
Remove original Comments sync line if you are using this approach
issue.comments = commentHelper.mergeComments(issue, replica){ it.executor = nodeHelper.getUserByEmail(it.author?.email) }
*/

/*
Status Synchronization

Sync status according to the mapping [remote issue status: local issue status]
If statuses are the same on both sides don't include them in the mapping
*/
def statusMapping = ["open":"To Do", "closed":"Done"]
def remoteStatusName = replica.status.name
issue.setStatus(statusMapping[remoteStatusName] ?: remoteStatusName)


/*
Custom Fields

This line will sync Text, Option(s), Number, Date, Organization, and Labels CFs
For other types of CF check documentation
issue.customFields."CF Name".value = replica.customFields."CF Name".value
*/
