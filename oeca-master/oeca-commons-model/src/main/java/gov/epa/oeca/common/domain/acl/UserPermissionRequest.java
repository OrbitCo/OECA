package gov.epa.oeca.common.domain.acl;

import com.fasterxml.jackson.annotation.JsonFormat;
import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "oeca_acl_user_permissions_req")
public class UserPermissionRequest extends UserPermissionBase {

    private static final long serialVersionUID = 1L;

    @Column(name = "request_status", nullable = false)
    @Enumerated(EnumType.STRING)
    RequestStatus requestStatus;

    @Column(name = "request_action", nullable = false)
    @Enumerated(EnumType.STRING)
    RequestAction requestAction;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "requester_user_id", nullable = false)
    User requester;

    @Column(name = "request_date", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    ZonedDateTime requestDate;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "reviewer_user_id")
    User reviewer;

    @Column(name = "review_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    ZonedDateTime reviewDate;

    @Column(name = "review_comment")
    String reviewComment;

    @Column(name = "notification_status", length = 1, nullable = false)
    @Enumerated(EnumType.STRING)
    NotificationStatus notificationStatus;
    
    @Column(name = "notification_retries", nullable = false)
    Long notificationRetries;

    @Override
    public String toString() {
        return "UserPermissionRequest{" +
                "requestStatus='" + requestStatus + '\'' +
                ", requestAction='" + requestAction + '\'' +
				", requester='" + requester + '\'' +
                ", requestDate='" + requestDate + '\'' +
                ", reviewer='" + reviewer + '\'' +
                ", reviewDate='" + reviewDate + '\'' +
                ", reviewComment='" + reviewComment + '\'' +
                ", notificationStatus='" + notificationStatus + '\'' +
                ", notificationRetries=" + notificationRetries +
                "} ";
    }

    public RequestStatus getRequestStatus() { return requestStatus; }

    public void setRequestStatus(RequestStatus requestStatus) { this.requestStatus = requestStatus; }

    public RequestAction getRequestAction() { return requestAction; }

    public void setRequestAction(RequestAction requestAction) { this.requestAction = requestAction; }

    public User getRequester() { return requester; }

    public void setRequester(User requester) { this.requester = requester; }

    public ZonedDateTime getRequestDate() { return requestDate; }

    public void setRequestDate(ZonedDateTime requestDate) { this.requestDate = requestDate; }

    public User getReviewer() { return reviewer; }

    public void setReviewer(User reviewer) { this.reviewer = reviewer; }

    public ZonedDateTime getReviewDate() { return reviewDate; }

    public void setReviewDate(ZonedDateTime reviewDate) { this.reviewDate = reviewDate; }

    public String getReviewComment() { return reviewComment; }

    public void setReviewComment(String reviewComment) { this.reviewComment = reviewComment; }

    public NotificationStatus getNotificationStatus() { return notificationStatus; }

	public void setNotificationStatus(NotificationStatus notificationStatus) { this.notificationStatus = notificationStatus; }

	public Long getNotificationRetries() { return notificationRetries; }

	public void setNotificationRetries(Long notificationRetries) { this.notificationRetries = notificationRetries; }

}
