package teammates.common.datatransfer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import teammates.common.util.Const;
import teammates.common.util.FieldValidator;
import teammates.common.util.Sanitizer;
import teammates.common.util.TimeHelper;
import teammates.common.util.Utils;
import teammates.storage.api.QuestionsDb;
import teammates.storage.entity.FeedbackSession;

import com.google.appengine.api.datastore.Text;

public class FeedbackSessionAttributes extends EntityAttributes implements SessionAttributes {
    private String feedbackSessionName;
    private String courseId;
    private String creatorEmail;
    private Text instructions;
    private Date createdTime;
    private Date startTime;
    private Date endTime;
    private Date sessionVisibleFromTime;
    private Date resultsVisibleFromTime;
    private double timeZone;
    private int gracePeriod;
    private FeedbackSessionType feedbackSessionType;
    private boolean sentOpenEmail;
    private boolean sentPublishedEmail;
    private boolean isOpeningEmailEnabled;
    private boolean isClosingEmailEnabled;
    private boolean isPublishedEmailEnabled;
    private Set<String> respondingInstructorList;
    private Set<String> respondingStudentList;
    
    private List<FeedbackQuestionAttributes> questions;

    public FeedbackSessionAttributes() {
        this.isOpeningEmailEnabled = true;
        this.isClosingEmailEnabled = true;
        this.isPublishedEmailEnabled = true;
        this.respondingInstructorList = new HashSet<String>();
        this.respondingStudentList = new HashSet<String>();
        this.questions = new ArrayList<>();
    }

    public FeedbackSessionAttributes(FeedbackSession fs) {
        this.feedbackSessionName = fs.getFeedbackSessionName();
        this.courseId = fs.getCourseId();
        this.creatorEmail = fs.getCreatorEmail();
        this.instructions = fs.getInstructions();
        this.createdTime = fs.getCreatedTime();
        this.startTime = fs.getStartTime();
        this.endTime = fs.getEndTime();
        this.sessionVisibleFromTime = fs.getSessionVisibleFromTime();
        this.resultsVisibleFromTime = fs.getResultsVisibleFromTime();
        this.timeZone = fs.getTimeZone();
        this.gracePeriod = fs.getGracePeriod();
        this.feedbackSessionType = fs.getFeedbackSessionType();
        this.sentOpenEmail = fs.isSentOpenEmail();
        this.sentPublishedEmail = fs.isSentPublishedEmail();
        this.isOpeningEmailEnabled = fs.isOpeningEmailEnabled();
        this.isClosingEmailEnabled = fs.isClosingEmailEnabled();
        this.isPublishedEmailEnabled = fs.isPublishedEmailEnabled();
        this.respondingInstructorList = fs.getRespondingInstructorList() == null ? new HashSet<String>()
                                                                                 : fs.getRespondingInstructorList();
        this.respondingStudentList = fs.getRespondingStudentList() == null ? new HashSet<String>()
                                                                           : fs.getRespondingStudentList();
        this.questions = fs.getFeedbackQuestions() == null
                         ? new ArrayList<FeedbackQuestionAttributes>()
                         : QuestionsDb.getListOfQuestionAttributes(fs.getFeedbackQuestions());
    }

    public FeedbackSessionAttributes(String feedbackSessionName, String courseId, String creatorId,
                                     Text instructions, Date createdTime, Date startTime, Date endTime,
                                     Date sessionVisibleFromTime, Date resultsVisibleFromTime,
                                     double timeZone, int gracePeriod, FeedbackSessionType feedbackSessionType,
                                     boolean sentOpenEmail, boolean sentPublishedEmail,
                                     boolean isOpeningEmailEnabled, boolean isClosingEmailEnabled,
                                     boolean isPublishedEmailEnabled) {
        this(feedbackSessionName, courseId, creatorId, instructions, createdTime, startTime, endTime,
             sessionVisibleFromTime, resultsVisibleFromTime, timeZone, gracePeriod, feedbackSessionType,
             sentOpenEmail, sentPublishedEmail, isOpeningEmailEnabled, isClosingEmailEnabled, isPublishedEmailEnabled,
             new HashSet<String>(), new HashSet<String>());
    }

    public FeedbackSessionAttributes(String feedbackSessionName, String courseId, String creatorId,
                                     Text instructions, Date createdTime, Date startTime, Date endTime,
                                     Date sessionVisibleFromTime, Date resultsVisibleFromTime,
                                     double timeZone, int gracePeriod, FeedbackSessionType feedbackSessionType,
                                     boolean sentOpenEmail, boolean sentPublishedEmail,
                                     boolean isOpeningEmailEnabled, boolean isClosingEmailEnabled,
                                     boolean isPublishedEmailEnabled, Set<String> instructorList,
                                     Set<String> studentList) {
        this.feedbackSessionName = feedbackSessionName;
        this.courseId = courseId;
        this.creatorEmail = creatorId;
        this.instructions = instructions == null ? null : new Text(Sanitizer.sanitizeForRichText(instructions.getValue()));
        this.createdTime = createdTime;
        this.startTime = startTime;
        this.endTime = endTime;
        this.sessionVisibleFromTime = sessionVisibleFromTime;
        this.resultsVisibleFromTime = resultsVisibleFromTime;
        this.timeZone = timeZone;
        this.gracePeriod = gracePeriod;
        this.feedbackSessionType = feedbackSessionType;
        this.sentOpenEmail = sentOpenEmail;
        this.sentPublishedEmail = sentPublishedEmail;
        this.isOpeningEmailEnabled = isOpeningEmailEnabled;
        this.isClosingEmailEnabled = isClosingEmailEnabled;
        this.isPublishedEmailEnabled = isPublishedEmailEnabled;
        this.respondingInstructorList = instructorList;
        this.respondingStudentList = studentList;
    }
    
    private FeedbackSessionAttributes(FeedbackSessionAttributes other) {
        this(other.feedbackSessionName, other.courseId, other.creatorEmail,
             other.instructions, other.createdTime, other.startTime, other.endTime,
             other.sessionVisibleFromTime, other.resultsVisibleFromTime, other.timeZone,
             other.gracePeriod, other.feedbackSessionType,
             other.sentOpenEmail, other.sentPublishedEmail,
             other.isOpeningEmailEnabled, other.isClosingEmailEnabled,
             other.isPublishedEmailEnabled, other.respondingInstructorList,
             other.respondingStudentList);
    }
    
    public static String makeId(String feedbackSessionName, String courseId) {
        return feedbackSessionName + "%" + courseId;
    }
        
    public FeedbackSessionAttributes getCopy() {
        return new FeedbackSessionAttributes(this);
    }

    public String getCourseId() {
        return courseId;
    }
    
    public String getFeedbackSessionName() {
        return feedbackSessionName;
    }
    
    public String getStartTimeString() {
        return TimeHelper.formatTime12H(startTime);
    }
    
    public String getEndTimeString() {
        return TimeHelper.formatTime12H(endTime);
    }
    
    public String getInstructionsString() {
        return Sanitizer.sanitizeForRichText(instructions.getValue());
    }

    @Override
    public FeedbackSession toEntity() {
        return new FeedbackSession(feedbackSessionName, courseId, creatorEmail, instructions, createdTime,
                                   startTime, endTime, sessionVisibleFromTime, resultsVisibleFromTime,
                                   timeZone, gracePeriod, feedbackSessionType, sentOpenEmail, sentPublishedEmail,
                                   isOpeningEmailEnabled, isClosingEmailEnabled, isPublishedEmailEnabled,
                                   respondingInstructorList, respondingStudentList,
                                   QuestionsDb.getListOfQuestionEntities(questions));
    }

    @Override
    public String getIdentificationString() {
        return this.feedbackSessionName + "/" + this.courseId;
    }

    @Override
    public String getEntityTypeAsString() {
        return "Feedback Session";
    }

    @Override
    public String getBackupIdentifier() {
        return Const.SystemParams.COURSE_BACKUP_LOG_MSG + courseId;
    }

    @Override
    public String getJsonString() {
        return Utils.getTeammatesGson().toJson(this, FeedbackSessionAttributes.class);
    }

    @Override
    public List<String> getInvalidityInfo() {
        FieldValidator validator = new FieldValidator();
        List<String> errors = new ArrayList<String>();
        String error;

        // Check for null fields.

        error = validator.getValidityInfoForNonNullField("feedback session name", feedbackSessionName);
        if (!error.isEmpty()) {
            errors.add(error);
        }

        error = validator.getValidityInfoForNonNullField("course ID", courseId);
        if (!error.isEmpty()) {
            errors.add(error);
        }

        error = validator.getValidityInfoForNonNullField("instructions to students", instructions);
        if (!error.isEmpty()) {
            errors.add(error);
        }

        error = validator.getValidityInfoForNonNullField("time for the session to become visible", sessionVisibleFromTime);
        if (!error.isEmpty()) {
            errors.add(error);
        }

        error = validator.getValidityInfoForNonNullField("creator's email", creatorEmail);
        if (!error.isEmpty()) {
            errors.add(error);
        }

        error = validator.getValidityInfoForNonNullField("session creation time", createdTime);
        if (!error.isEmpty()) {
            errors.add(error);
        }

        // Early return if any null fields
        if (!errors.isEmpty()) {
            return errors;
        }

        error = validator.getInvalidityInfoForFeedbackSessionName(feedbackSessionName);
        if (!error.isEmpty()) {
            errors.add(error);
        }

        error = validator.getInvalidityInfoForCourseId(courseId);
        if (!error.isEmpty()) {
            errors.add(error);
        }

        error = validator.getInvalidityInfoForEmail(creatorEmail);
        if (!error.isEmpty()) {
            errors.add("Invalid creator's email: " + error);
        }

        // Skip time frame checks if session type is private.
        if (this.isPrivateSession()) {
            return errors;
        }

        error = validator.getValidityInfoForNonNullField("submission opening time", startTime);
        if (!error.isEmpty()) {
            errors.add(error);
        }

        error = validator.getValidityInfoForNonNullField("submission closing time", endTime);
        if (!error.isEmpty()) {
            errors.add(error);
        }

        error = validator.getValidityInfoForNonNullField("time for the responses to become visible", resultsVisibleFromTime);
        if (!error.isEmpty()) {
            errors.add(error);
        }

        // Early return if any null fields
        if (!errors.isEmpty()) {
            return errors;
        }

        error = validator.getInvalidityInfoForTimeForSessionStartAndEnd(startTime, endTime);
        if (!error.isEmpty()) {
            errors.add(error);
        }

        error = validator.getInvalidityInfoForTimeForVisibilityStartAndSessionStart(
                              sessionVisibleFromTime, startTime);
        if (!error.isEmpty()) {
            errors.add(error);
        }

        Date actualSessionVisibleFromTime = sessionVisibleFromTime;

        if (actualSessionVisibleFromTime.equals(Const.TIME_REPRESENTS_FOLLOW_OPENING)) {
            actualSessionVisibleFromTime = startTime;
        }

        error = validator.getInvalidityInfoForTimeForVisibilityStartAndResultsPublish(
                              actualSessionVisibleFromTime, resultsVisibleFromTime);
        if (!error.isEmpty()) {
            errors.add(error);
        }

        return errors;
    }

    @Override
    public boolean isValid() {
        return getInvalidityInfo().isEmpty();
    }

    public boolean isClosingWithinTimeLimit(int hours) {
        Calendar now = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        // Fix the time zone accordingly
        now.add(Calendar.MILLISECOND,
                (int) (60 * 60 * 1000 * timeZone));

        Calendar start = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        start.setTime(startTime);

        Calendar deadline = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        deadline.setTime(endTime);

        long nowMillis = now.getTimeInMillis();
        long deadlineMillis = deadline.getTimeInMillis();
        long differenceBetweenDeadlineAndNow = (deadlineMillis - nowMillis) / (60 * 60 * 1000);

        // If now and start are almost similar, it means the feedback session
        // is open for only 24 hours.
        // Hence we do not send a reminder e-mail for feedback session.
        return now.after(start)
               && differenceBetweenDeadlineAndNow >= hours - 1
               && differenceBetweenDeadlineAndNow < hours;
    }

    /**
     * @return {@code true} if it is after the closing time of this feedback session; {@code false} if not.
     */
    public boolean isClosed() {
        Calendar now = TimeHelper.now(timeZone);
        Calendar end = TimeHelper.dateToCalendar(endTime);
        end.add(Calendar.MINUTE, gracePeriod);

        return now.after(end);
    }

    /**
     * @return {@code true} is currently open and accepting responses
     */
    public boolean isOpened() {
        Calendar now = TimeHelper.now(timeZone);
        Calendar start = TimeHelper.dateToCalendar(startTime);
        Calendar end = TimeHelper.dateToCalendar(endTime);

        return now.after(start) && now.before(end);
    }

    /**
     * @return {@code true} is currently close but is still accept responses
     */
    public boolean isInGracePeriod() {
        Calendar now = TimeHelper.now(timeZone);
        Calendar end = TimeHelper.dateToCalendar(endTime);
        Calendar gracedEnd = TimeHelper.dateToCalendar(endTime);
        gracedEnd.add(Calendar.MINUTE, gracePeriod);

        return now.after(end) && now.before(gracedEnd);
    }

    /**
     * @return {@code true} has not opened before and is waiting to open.<br>
     * {@code false} if session has opened before.
     */
    public boolean isWaitingToOpen() {
        Calendar now = TimeHelper.now(timeZone);
        Calendar start = TimeHelper.dateToCalendar(startTime);

        return now.before(start);
    }

    /**
     * @return {@code true} if the session is visible; {@code false} if not.
     * Does not care if the session has started or not.
     */
    public boolean isVisible() {
        Date visibleTime = this.sessionVisibleFromTime;

        if (visibleTime.equals(Const.TIME_REPRESENTS_FOLLOW_OPENING)) {
            visibleTime = this.startTime;
        } else if (visibleTime.equals(Const.TIME_REPRESENTS_NEVER)) {
            return false;
        }

        Date now = TimeHelper.now(timeZone).getTime();
        return visibleTime.before(now);
    }

    /**
     * @return {@code true} if the results of the feedback session is visible; {@code false} if not.
     * Does not care if the session has ended or not.
     */
    public boolean isPublished() {
        Date now = TimeHelper.now(timeZone).getTime();
        Date publishTime = this.resultsVisibleFromTime;

        if (publishTime.equals(Const.TIME_REPRESENTS_FOLLOW_VISIBLE)) {
            return isVisible();
        } else if (publishTime.equals(Const.TIME_REPRESENTS_LATER)) {
            return false;
        } else if (publishTime.equals(Const.TIME_REPRESENTS_NEVER)) {
            return false;
        } else if (publishTime.equals(Const.TIME_REPRESENTS_NOW)) {
            return true;
        } else {
            return publishTime.before(now);
        }
    }

    /**
     * @return {@code true} if the session has been set by the creator to be manually published.
     */
    public boolean isManuallyPublished() {
        return resultsVisibleFromTime.equals(Const.TIME_REPRESENTS_LATER)
               || resultsVisibleFromTime.equals(Const.TIME_REPRESENTS_NOW);
    }

    /**
     * @return {@code true} if session is a private session (only open to the session creator),
     *  {@code false} if not.
     */
    public boolean isPrivateSession() {
        return sessionVisibleFromTime.equals(Const.TIME_REPRESENTS_NEVER)
               || feedbackSessionType.equals(FeedbackSessionType.PRIVATE);
    }

    public boolean isCreator(String instructorEmail) {
        return creatorEmail.equals(instructorEmail);
    }

    @Override
    public void sanitizeForSaving() {

        if (instructions != null) {
            this.instructions = new Text(Sanitizer.sanitizeForRichText(instructions.getValue()));
        }
    }

    @Override
    public String toString() {
        return "FeedbackSessionAttributes [feedbackSessionName="
               + feedbackSessionName + ", courseId=" + courseId
               + ", creatorEmail=" + creatorEmail + ", instructions=" + instructions
               + ", startTime=" + startTime
               + ", endTime=" + endTime + ", sessionVisibleFromTime="
               + sessionVisibleFromTime + ", resultsVisibleFromTime="
               + resultsVisibleFromTime + ", timeZone=" + timeZone
               + ", gracePeriod=" + gracePeriod + ", feedbackSessionType="
               + feedbackSessionType + ", sentOpenEmail=" + sentOpenEmail
               + ", sentPublishedEmail=" + sentPublishedEmail
               + ", isOpeningEmailEnabled=" + isOpeningEmailEnabled
               + ", isClosingEmailEnabled=" + isClosingEmailEnabled
               + ", isPublishedEmailEnabled=" + isPublishedEmailEnabled + "]";
    }

    /**
     * Sorts feedback session based courseID (ascending), then by create time (ascending), deadline
     * (ascending), then by start time (ascending), then by feedback session name
     * (ascending). The sort by CourseID part is to cater the case when this
     * method is called with combined feedback sessions from many courses
     *
     * @param sessions
     */
    public static void sortFeedbackSessionsByCreationTime(List<FeedbackSessionAttributes> sessions) {
        Collections.sort(sessions, new Comparator<FeedbackSessionAttributes>() {
            @Override
            public int compare(FeedbackSessionAttributes session1, FeedbackSessionAttributes session2) {
                int result = session1.courseId.compareTo(session2.courseId);

                if (result == 0) {
                    result = session1.createdTime.compareTo(session2.createdTime);
                }

                if (result == 0) {
                    result = session1.endTime.compareTo(session2.endTime);
                }

                if (result == 0) {
                    result = session1.startTime.compareTo(session2.startTime);
                }

                if (result == 0) {
                    result = session1.feedbackSessionName.compareTo(session2.feedbackSessionName);
                }

                return result;
            }
        });
    }

    /**
     * Sorts feedback session based on create time (descending), deadline
     * (descending), then by start time (descending),then by courseID (ascending),then by feedback session name
     * (ascending). The sort by CourseID part is to cater the case when this
     * method is called with combined feedback sessions from many courses
     *
     * @param sessions
     */
    public static void sortFeedbackSessionsByCreationTimeDescending(List<FeedbackSessionAttributes> sessions) {
        Collections.sort(sessions, new Comparator<FeedbackSessionAttributes>() {
            @Override
            public int compare(FeedbackSessionAttributes session1, FeedbackSessionAttributes session2) {
                int result = session2.createdTime.compareTo(session1.createdTime);
                if (result == 0) {
                    if (session1.endTime == null || session2.endTime == null) {
                        if (session1.endTime == null) {
                            --result;
                        }
                        if (session2.endTime == null) {
                            ++result;
                        }
                    } else {
                        result = session2.endTime.compareTo(session1.endTime);
                    }
                }

                if (result == 0) {
                    result = session2.startTime.compareTo(session1.startTime);
                }
                if (result == 0) {
                    result = session1.courseId.compareTo(session2.courseId);
                }

                if (result == 0) {
                    result = session1.feedbackSessionName.compareTo(session2.feedbackSessionName);
                }
                
                return result;
            }
        });
    }

    @Override
    public Date getSessionStartTime() {
        return this.startTime;
    }

    @Override
    public Date getSessionEndTime() {
        return this.endTime;
    }

    @Override
    public String getSessionName() {
        return this.feedbackSessionName;
    }

    public void setFeedbackSessionName(String feedbackSessionName) {
        this.feedbackSessionName = feedbackSessionName;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCreatorEmail() {
        return creatorEmail;
    }

    public void setCreatorEmail(String creatorEmail) {
        this.creatorEmail = creatorEmail;
    }

    public Text getInstructions() {
        return instructions;
    }

    public void setInstructions(Text instructions) {
        this.instructions = instructions;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getSessionVisibleFromTime() {
        return sessionVisibleFromTime;
    }

    public void setSessionVisibleFromTime(Date sessionVisibleFromTime) {
        this.sessionVisibleFromTime = sessionVisibleFromTime;
    }

    public Date getResultsVisibleFromTime() {
        return resultsVisibleFromTime;
    }

    public void setResultsVisibleFromTime(Date resultsVisibleFromTime) {
        this.resultsVisibleFromTime = resultsVisibleFromTime;
    }

    public double getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(double timeZone) {
        this.timeZone = timeZone;
    }

    public int getGracePeriod() {
        return gracePeriod;
    }

    public void setGracePeriod(int gracePeriod) {
        this.gracePeriod = gracePeriod;
    }

    public FeedbackSessionType getFeedbackSessionType() {
        return feedbackSessionType;
    }

    public void setFeedbackSessionType(FeedbackSessionType feedbackSessionType) {
        this.feedbackSessionType = feedbackSessionType;
    }

    public boolean isSentOpenEmail() {
        return sentOpenEmail;
    }

    public void setSentOpenEmail(boolean sentOpenEmail) {
        this.sentOpenEmail = sentOpenEmail;
    }

    public boolean isSentPublishedEmail() {
        return sentPublishedEmail;
    }

    public void setSentPublishedEmail(boolean sentPublishedEmail) {
        this.sentPublishedEmail = sentPublishedEmail;
    }

    public boolean isOpeningEmailEnabled() {
        return isOpeningEmailEnabled;
    }

    public void setOpeningEmailEnabled(boolean isOpeningEmailEnabled) {
        this.isOpeningEmailEnabled = isOpeningEmailEnabled;
    }

    public boolean isClosingEmailEnabled() {
        return isClosingEmailEnabled;
    }

    public void setClosingEmailEnabled(boolean isClosingEmailEnabled) {
        this.isClosingEmailEnabled = isClosingEmailEnabled;
    }

    public boolean isPublishedEmailEnabled() {
        return isPublishedEmailEnabled;
    }

    public void setPublishedEmailEnabled(boolean isPublishedEmailEnabled) {
        this.isPublishedEmailEnabled = isPublishedEmailEnabled;
    }

    public Set<String> getRespondingInstructorList() {
        return respondingInstructorList;
    }

    public void setRespondingInstructorList(Set<String> respondingInstructorList) {
        this.respondingInstructorList = respondingInstructorList;
    }

    public Set<String> getRespondingStudentList() {
        return respondingStudentList;
    }

    public void setRespondingStudentList(Set<String> respondingStudentList) {
        this.respondingStudentList = respondingStudentList;
    }
    
    public List<FeedbackQuestionAttributes> getQuestions() {
        return questions;
    }
    
    public void setQuestions(List<FeedbackQuestionAttributes> questions) {
        this.questions = questions;
    }
}
