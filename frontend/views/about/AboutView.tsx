import css from './AboutView.module.css';
export default function AboutView() {
  return (
    <div className="flex flex-col h-full items-center justify-center p-l text-center box-border">
      <img className={css.img} src="images/Jeremy.png" alt="Jeremy sitting in chair"/>
      <h2>This place intentionally left empty</h2>
      <p>It’s a place where you can grow your own UI 🤗</p>
    </div>
  );
}
