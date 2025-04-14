public class Review {
    private boolean like;
    private boolean dislike;
    private String comment;

    public Review() {
        this.like = false;
        this.dislike = false;
        this.comment = "";
    }

    public Review(boolean like, boolean dislike, String comment) {
        this.like = like;
        this.dislike = dislike;
        this.comment = comment;
    }

    public boolean submitComment() {
        // Simple implementation: if a comment is provided, simulate submission.
        if (comment != null && !comment.trim().isEmpty()) {
            System.out.println("Comment submitted: " + comment);
            return true;
        } else {
            System.out.println("No comment to submit.");
            return false;
        }
    }

    public boolean submitDislike() {
        if (!dislike) {
            dislike = true;
            System.out.println("Dislike submitted.");
            return true;
        } else {
            System.out.println("Dislike already submitted.");
            return false;
        }
    }
    
    public boolean submitLike() {
        if (!like) {
            like = true;
            System.out.println("Like submitted.");
            return true;
        } else {
            System.out.println("Like already submitted.");
            return false;
        }
    }

    // Getters and Setters

    public boolean isLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }

    public boolean isDislike() {
        return dislike;
    }

    public void setDislike(boolean dislike) {
        this.dislike = dislike;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
