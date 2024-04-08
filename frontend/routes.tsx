import MainLayout from 'Frontend/views/MainLayout.js';
import { lazy } from 'react';
import { createBrowserRouter, IndexRouteObject, NonIndexRouteObject, useMatches } from 'react-router-dom';
import MainView from "Frontend/views/main/MainView";

const AboutView = lazy(async () => import('Frontend/views/about/AboutView.js'));
const ChatView = lazy(async () => import('Frontend/views/chat/ChatView.js'));
const ReactiveView = lazy(async () => import('Frontend/views/Reactive/ReactiveView'));

export type MenuProps = Readonly<{
  icon?: string;
  title?: string;
}>;

export type ViewMeta = Readonly<{ handle?: MenuProps }>;

type Override<T, E> = Omit<T, keyof E> & E;

export type IndexViewRouteObject = Override<IndexRouteObject, ViewMeta>;
export type NonIndexViewRouteObject = Override<
  Override<NonIndexRouteObject, ViewMeta>,
  {
    children?: ViewRouteObject[];
  }
>;
export type ViewRouteObject = IndexViewRouteObject | NonIndexViewRouteObject;

type RouteMatch = ReturnType<typeof useMatches> extends (infer T)[] ? T : never;

export type ViewRouteMatch = Readonly<Override<RouteMatch, ViewMeta>>;

export const useViewMatches = useMatches as () => readonly ViewRouteMatch[];

export const routes: readonly ViewRouteObject[] = [
  {
    element: <MainLayout />,
    handle: { icon: 'null', title: 'Main' },
    children: [
      { path: '/', element: <MainView />, handle: { icon: 'globe-solid', title: 'Main' } },
      { path: '/about', element: <AboutView />, handle: { icon: 'file', title: 'About' } },
      { path: '/chat', element: <ChatView />, handle: { icon: 'robot-solid', title: 'Chat' } },
      { path: '/reactive', element: <ReactiveView />, handle: { icon: 'react', title: 'Reactive' } },
    ],
  },
];

const router = createBrowserRouter([...routes]);
export default router;
