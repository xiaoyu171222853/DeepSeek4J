package top.aoyudi.rag;

public interface VectorSimilarityCalc {

    float calcSimilarity(float[] vectorA, float[] vectorB);
}
