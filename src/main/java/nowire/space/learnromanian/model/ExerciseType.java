package nowire.space.learnromanian.model;

import nowire.space.learnromanian.util.ExerciseContent;

public enum ExerciseType {


    TEST("T_G"){
        public String getExercise (){
            return ExerciseContent.builder().build().getContentT_G();
        }
    },
    COMPLETE_SENTENCES ("C_S"){
        @Override
        public String getExercise() {
            return ExerciseContent.builder().build().getContentC_S();
        }
    },
    WRITE_SENTENCES ("W_S"){
        @Override
        public String getExercise() {
            return ExerciseContent.builder().build().getContentW_S();
        }
    };

    public String getExercise(){
        return "No exercises selected !";
    }

    private String code;

    ExerciseType(String code) {
        this.code=code;
    }

    public String getCode() {
        return code;
    }

}
