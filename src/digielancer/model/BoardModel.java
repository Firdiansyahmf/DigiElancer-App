package digielancer.model;

public class BoardModel {
    private int id;
    private int projectId;
    private String boardName;
    private String description;
    private boolean isCompletionBoard;

    public BoardModel(int id, int projectId, String boardName, String description, boolean isCompletionBoard) {
        this.id = id;
        this.projectId = projectId;
        this.boardName = boardName;
        this.description = description;
        this.isCompletionBoard = isCompletionBoard;
    }

    public int getId() { return id; }
    public int getProjectId() { return projectId; }
    public String getBoardName() { return boardName; }
    public String getDescription() { return description; }
    public boolean isCompletionBoard() { return isCompletionBoard; }

    @Override
    public String toString() {
        return boardName;
    }
}
