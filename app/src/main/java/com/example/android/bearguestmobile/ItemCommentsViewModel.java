package com.example.android.bearguestmobile;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

public class ItemCommentsViewModel extends ViewModel {
    private LiveData<List<Review>> commentListLiveData;
    private LiveData<List<MenuItem>> menuFavoritesList;
    private int isSelectedItemFavorite;
    private MutableLiveData<Boolean> hasCommentForItem = new MutableLiveData<>();

    public ItemCommentsViewModel() { }

    // Test method: returns all Reviews in the database
    public LiveData<List<Review>> getAllReviews() {
        this.commentListLiveData = ItemCommentsRepository.getInstance().getAllReviews();
        return this.commentListLiveData;
    }

    public LiveData<List<Review>> getAllReviewsByItem(ItemID itemID) {
        this.commentListLiveData = ItemCommentsRepository.getInstance().getAllReviewsByItem(itemID);
        return this.commentListLiveData;
    }

    public void addNewComment(AddReview newReview, ItemID itemID) {
        ItemCommentsRepository.getInstance().addReviewComment(newReview);
        //this.commentListLiveData = ItemCommentsRepository.getInstance().getAllReviewsByItem(itemID);
    }

    public void updateCommentList(ItemID itemID) {
        this.commentListLiveData = ItemCommentsRepository.getInstance().getAllReviewsByItem(itemID);
    }

    public LiveData<List<MenuItem>> getUserFavoritedItems(Uid uid) {
        this.menuFavoritesList = ItemCommentsRepository.getInstance().getUserFavoritedItems(uid);
        return this.menuFavoritesList;
    }

    public int getIsSelectedItemFavorite() { return this.isSelectedItemFavorite; }

    public void setIsSelectedItemFavorite(int data) {
        this.isSelectedItemFavorite = data;
    }

    public LiveData<Boolean> getHasCommentForItem() { return this.hasCommentForItem; }

    public void setHasCommentForItem(Boolean hasCommentForItem) { this.hasCommentForItem.setValue(hasCommentForItem); }

    public void updateFavoriteStatus(Favorite favorite) {
        ItemCommentsRepository.getInstance().updateFavoriteStatus(favorite);
    }

    public void deletePublicReview(Review review) {
        ItemCommentsRepository.getInstance().deletePublicReview(review);
    }

    public void deleteEntireReview(Review review) {
        ItemCommentsRepository.getInstance().deleteEntireReview(review);
    }
}
